package com.reliaquest.api.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class EmployeeClientTest {

    @InjectMocks
    private EmployeeClient employeeClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(employeeClient, "empClientBaseUrl", "http://fake-api.com/employees");
    }

    @Test
    void testGetAllEmployees_success() throws ServiceException, RateLimitException {
        EmployeeResponseBean emp = new EmployeeResponseBean();
        EmployeesData data = new EmployeesData();
        data.setData(List.of(emp));

        ResponseEntity<EmployeesData> responseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        when(restTemplate.exchange(
                        eq("http://fake-api.com/employees"),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<EmployeesData>>any()))
                .thenReturn(responseEntity);

        List<EmployeeResponseBean> result = employeeClient.getAllEmployees();

        assertEquals(1, result.size());
        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void testGetAllEmployees_rateLimit() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(HttpClientErrorException.TooManyRequests.class);

        assertThrows(RateLimitException.class, () -> employeeClient.getAllEmployees());
    }

    @Test
    void testGetEmployeeById_success() throws ServiceException, RateLimitException, NotFoundException {
        EmployeeResponseBean emp = new EmployeeResponseBean();
        EmployeeData empData = new EmployeeData();
        empData.setData(emp);

        ResponseEntity<EmployeeData> responseEntity = new ResponseEntity<>(empData, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(EmployeeData.class))).thenReturn(responseEntity);

        EmployeeResponseBean result = employeeClient.getEmployeeById("123");
        assertNotNull(result);
    }

    @Test
    void testGetEmployeeById_notFound() {
        when(restTemplate.getForEntity(anyString(), eq(EmployeeData.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        assertThrows(NotFoundException.class, () -> employeeClient.getEmployeeById("notexist"));
    }

    @Test
    void testCreateEmployee_success() throws ServiceException, RateLimitException {
        EmployeeCreateRequest request = new EmployeeCreateRequest();
        request.setName("Test");

        EmployeeResponseBean emp = new EmployeeResponseBean();
        EmployeeData data = new EmployeeData();
        data.setData(emp);

        ResponseEntity<EmployeeData> responseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(EmployeeData.class)))
                .thenReturn(responseEntity);

        EmployeeResponseBean result = employeeClient.createEmployee(request);
        assertNotNull(result);
    }

    @Test
    void testDeleteEmployeeById_success() throws ServiceException, RateLimitException {
        EmployeeDeleteRequest deleteRequest = new EmployeeDeleteRequest("test-user");

        EmployeeDeleteResponse response = new EmployeeDeleteResponse();
        response.setStatus("deleted");

        ResponseEntity<EmployeeDeleteResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(
                        eq("http://fake-api.com/employees"),
                        eq(HttpMethod.DELETE),
                        any(HttpEntity.class),
                        ArgumentMatchers.<ParameterizedTypeReference<EmployeeDeleteResponse>>any()))
                .thenReturn(responseEntity);

        EmployeeDeleteResponse result = employeeClient.deleteEmployeeById(deleteRequest);
        assertEquals("deleted", result.getStatus());
    }

    @Test
    void testDeleteEmployeeById_rateLimit() {
        EmployeeDeleteRequest deleteRequest = new EmployeeDeleteRequest("any");

        when(restTemplate.exchange(
                        anyString(),
                        eq(HttpMethod.DELETE),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class)))
                .thenThrow(HttpClientErrorException.TooManyRequests.class);

        assertThrows(RateLimitException.class, () -> employeeClient.deleteEmployeeById(deleteRequest));
    }
}
