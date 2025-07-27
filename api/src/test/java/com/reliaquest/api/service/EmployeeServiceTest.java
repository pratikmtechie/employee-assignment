package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.client.IEmpolyeeClient;
import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private IEmpolyeeClient employeeClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private EmployeeResponseBean createEmployee(String name, int salary) {
        return EmployeeResponseBean.builder()
                .employeeName(name)
                .employeeSalary(salary)
                .build();
    }

    @Test
    void getAllEmployees_success() throws ServiceException, RateLimitException {
        List<EmployeeResponseBean> employees = List.of(createEmployee("John", 5000), createEmployee("Jane", 6000));

        when(employeeClient.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<EmployeeResponseBean>> response = employeeService.getAllEmployees();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(employeeClient, times(2)).getAllEmployees();
    }

    @Test
    void getAllEmployees_empty_throwsServiceException() throws ServiceException, RateLimitException {
        when(employeeClient.getAllEmployees()).thenReturn(new ArrayList<>());

        ServiceException thrown = assertThrows(ServiceException.class, () -> employeeService.getAllEmployees());

        assertTrue(thrown.getMessage().contains("EMP_LIST_EMPTY"));
    }

    @Test
    void getEmployeesByNameSearch_found() throws Exception {
        List<EmployeeResponseBean> employees =
                List.of(createEmployee("John", 5000), createEmployee("Johnny", 6000), createEmployee("Jane", 4000));
        when(employeeClient.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<EmployeeResponseBean>> response = employeeService.getEmployeesByNameSearch("john");

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getEmployeesByNameSearch_notFound_throwsNotFoundException() throws Exception {
        when(employeeClient.getAllEmployees()).thenReturn(List.of(createEmployee("Jane", 4000)));

        NotFoundException thrown =
                assertThrows(NotFoundException.class, () -> employeeService.getEmployeesByNameSearch("xyz"));

        assertTrue(thrown.getMessage().contains("EMP_NAME_NOT_PRESENT"));
    }

    @Test
    void getEmployeeById_success() throws Exception {
        EmployeeResponseBean employee = createEmployee("John", 5000);
        when(employeeClient.getEmployeeById("123")).thenReturn(employee);

        ResponseEntity<EmployeeResponseBean> response = employeeService.getEmployeeById("123");

        assertNotNull(response);
        assertEquals("John", response.getBody().getEmployeeName());
    }

    @Test
    void getHighestSalaryOfEmployees_success() throws Exception {
        List<EmployeeResponseBean> employees =
                List.of(createEmployee("John", 5000), createEmployee("Jane", 7000), createEmployee("Jake", 6000));
        when(employeeClient.getAllEmployees()).thenReturn(employees);

        ResponseEntity<Integer> response = employeeService.getHighestSalaryOfEmployees();

        assertEquals(7000, response.getBody());
    }

    @Test
    void getHighestSalaryOfEmployees_empty_throwsServiceException() throws ServiceException, RateLimitException {
        when(employeeClient.getAllEmployees()).thenReturn(new ArrayList<>());

        ServiceException thrown =
                assertThrows(ServiceException.class, () -> employeeService.getHighestSalaryOfEmployees());

        assertTrue(thrown.getMessage().contains("EMP_LIST_EMPTY"));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_success() throws Exception {
        List<EmployeeResponseBean> employees = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            employees.add(createEmployee("Employee" + i, i * 1000));
        }
        when(employeeClient.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<String>> response = employeeService.getTopTenHighestEarningEmployeeNames();

        assertEquals(10, response.getBody().size());
        assertEquals("Employee15", response.getBody().get(0));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_empty_throwsServiceException()
            throws ServiceException, RateLimitException {
        when(employeeClient.getAllEmployees()).thenReturn(new ArrayList<>());

        ServiceException thrown =
                assertThrows(ServiceException.class, () -> employeeService.getTopTenHighestEarningEmployeeNames());

        assertTrue(thrown.getMessage().contains("EMP_LIST_EMPTY"));
    }

    @Test
    void createEmployee_success() throws Exception {
        EmployeeRequestBean request = EmployeeRequestBean.builder()
                .employeeName("John")
                .employeeAge(30)
                .employeeSalary(5000)
                .employeeTitle("Developer")
                .build();

        EmployeeResponseBean responseBean = createEmployee("John", 5000);

        when(employeeClient.createEmployee(any(EmployeeCreateRequest.class))).thenReturn(responseBean);

        ResponseEntity<EmployeeResponseBean> response = employeeService.createEmployee(request);

        assertNotNull(response);
        assertEquals("John", response.getBody().getEmployeeName());
    }

    @Test
    void createEmployee_nullResponse_throwsServiceException() throws Exception {
        EmployeeRequestBean request = EmployeeRequestBean.builder()
                .employeeName("John")
                .employeeAge(30)
                .employeeSalary(5000)
                .employeeTitle("Developer")
                .build();

        when(employeeClient.createEmployee(any(EmployeeCreateRequest.class))).thenReturn(null);

        ServiceException thrown = assertThrows(ServiceException.class, () -> employeeService.createEmployee(request));

        assertTrue(thrown.getMessage().contains("EMP_CREATE_FAILED"));
    }

    @Test
    void deleteEmployeeById_success() throws Exception {
        EmployeeResponseBean employee = createEmployee("John", 5000);
        when(employeeClient.getEmployeeById("123")).thenReturn(employee);
        when(employeeClient.deleteEmployeeById(any(EmployeeDeleteRequest.class)))
                .thenReturn(new EmployeeDeleteResponse());

        ResponseEntity<String> response = employeeService.deleteEmployeeById("123");

        assertEquals("John", response.getBody());
    }
}
