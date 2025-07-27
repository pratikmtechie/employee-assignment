package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.EmployeeRequestBean;
import com.reliaquest.api.model.EmployeeResponseBean;
import com.reliaquest.api.service.IEmployeeService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private IEmployeeService employeeService;

    private EmployeeResponseBean mockEmployee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockEmployee = new EmployeeResponseBean();
        mockEmployee.setEmployeeName("John Doe");
    }

    @Test
    void testGetAllEmployees_success() throws ServiceException, RateLimitException {
        when(employeeService.getAllEmployees()).thenReturn(ResponseEntity.ok(List.of(mockEmployee)));

        var response = employeeController.getAllEmployees();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void testGetEmployeesByNameSearch_success() throws ServiceException, RateLimitException, NotFoundException {
        when(employeeService.getEmployeesByNameSearch("john")).thenReturn(ResponseEntity.ok(List.of(mockEmployee)));

        var response = employeeController.getEmployeesByNameSearch("john");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().get(0).getEmployeeName());
        verify(employeeService).getEmployeesByNameSearch("john");
    }

    @Test
    void testGetEmployeeById_success() throws ServiceException, RateLimitException, NotFoundException {
        when(employeeService.getEmployeeById("123")).thenReturn(ResponseEntity.ok(mockEmployee));

        var response = employeeController.getEmployeeById("123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getEmployeeName());
        verify(employeeService).getEmployeeById("123");
    }

    @Test
    void testGetHighestSalaryOfEmployees_success() throws ServiceException, RateLimitException {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(ResponseEntity.ok(90000));

        var response = employeeController.getHighestSalaryOfEmployees();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(90000, response.getBody());
        verify(employeeService).getHighestSalaryOfEmployees();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames_success() throws ServiceException, RateLimitException {
        List<String> names = List.of("John", "Alice", "Bob");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(ResponseEntity.ok(names));

        var response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().size());
        verify(employeeService).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    void testCreateEmployee_success() throws ServiceException, RateLimitException {
        EmployeeRequestBean request = new EmployeeRequestBean();
        request.setEmployeeName("New Emp");

        when(employeeService.createEmployee(request)).thenReturn(ResponseEntity.ok(mockEmployee));

        var response = employeeController.createEmployee(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getEmployeeName());
        verify(employeeService).createEmployee(request);
    }

    @Test
    void testDeleteEmployeeById_success() throws ServiceException, RateLimitException, NotFoundException {
        when(employeeService.deleteEmployeeById("123")).thenReturn(ResponseEntity.ok("Deleted"));

        var response = employeeController.deleteEmployeeById("123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted", response.getBody());
        verify(employeeService).deleteEmployeeById("123");
    }
}
