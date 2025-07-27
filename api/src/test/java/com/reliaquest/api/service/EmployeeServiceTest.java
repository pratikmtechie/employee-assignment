package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.client.IEmpolyeeClient;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.*;
import com.reliaquest.api.validator.EmployeeRequestBeanValidator;
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

    @Mock
    private EmployeeRequestBeanValidator employeeRequestBeanValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployees_shouldReturnEmployees() throws ServiceException, RateLimitException {
        List<EmployeeResponseBean> mockList = List.of(new EmployeeResponseBean());
        when(employeeClient.getAllEmployees()).thenReturn(mockList);

        ResponseEntity<List<EmployeeResponseBean>> response = employeeService.getAllEmployees();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(employeeClient, times(2)).getAllEmployees(); // Called twice
    }

    @Test
    void getEmployeesByNameSearch_shouldReturnMatching() throws Exception {
        EmployeeResponseBean emp = new EmployeeResponseBean();
        emp.setEmployeeName("John Doe");

        when(employeeClient.getAllEmployees()).thenReturn(List.of(emp));

        ResponseEntity<List<EmployeeResponseBean>> response = employeeService.getEmployeesByNameSearch("john");

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() throws Exception {
        EmployeeResponseBean emp = new EmployeeResponseBean();
        emp.setEmployeeName("John");

        when(employeeClient.getEmployeeById("123")).thenReturn(emp);

        ResponseEntity<EmployeeResponseBean> response = employeeService.getEmployeeById("123");

        assertEquals("John", response.getBody().getEmployeeName());
    }

    @Test
    void getHighestSalaryOfEmployees_shouldReturnMax() throws Exception {
        EmployeeResponseBean e1 = new EmployeeResponseBean();
        e1.setEmployeeSalary(1000);
        EmployeeResponseBean e2 = new EmployeeResponseBean();
        e2.setEmployeeSalary(2000);

        when(employeeClient.getAllEmployees()).thenReturn(List.of(e1, e2));

        ResponseEntity<Integer> response = employeeService.getHighestSalaryOfEmployees();

        assertEquals(2000, response.getBody());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_shouldReturnTopTen() throws Exception {
        List<EmployeeResponseBean> employees =
                List.of(createEmployee("A", 100), createEmployee("B", 200), createEmployee("C", 300));

        when(employeeClient.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<String>> response = employeeService.getTopTenHighestEarningEmployeeNames();

        assertEquals(List.of("C", "B", "A"), response.getBody());
    }

    @Test
    void createEmployee_shouldReturnCreated() throws Exception {
        EmployeeRequestBean req = new EmployeeRequestBean();
        req.setEmployeeName("John");
        req.setEmployeeAge(30);
        req.setEmployeeSalary(50000);
        req.setEmployeeTitle("Engineer");

        when(employeeRequestBeanValidator.validate(any(), anyList())).thenReturn(true);
        when(employeeClient.createEmployee(any())).thenReturn(new EmployeeResponseBean());

        ResponseEntity<EmployeeResponseBean> response = employeeService.createEmployee(req);

        assertNotNull(response.getBody());
    }

    @Test
    void deleteEmployeeById_shouldReturnName() throws Exception {
        EmployeeResponseBean emp = new EmployeeResponseBean();
        emp.setEmployeeName("John");

        when(employeeClient.getEmployeeById("123")).thenReturn(emp);
        when(employeeClient.deleteEmployeeById(any())).thenReturn(new EmployeeDeleteResponse());

        ResponseEntity<String> response = employeeService.deleteEmployeeById("123");

        assertEquals("John", response.getBody());
    }

    // Utility method
    private EmployeeResponseBean createEmployee(String name, int salary) {
        EmployeeResponseBean bean = new EmployeeResponseBean();
        bean.setEmployeeName(name);
        bean.setEmployeeSalary(salary);
        return bean;
    }
}
