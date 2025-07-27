package com.reliaquest.api.controller;

import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.EmployeeRequestBean;
import com.reliaquest.api.model.EmployeeResponseBean;
import com.reliaquest.api.service.IEmployeeService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/employee")
public class EmployeeController implements IEmployeeController<EmployeeResponseBean, EmployeeRequestBean> {

    @Autowired
    private IEmployeeService employeeService;

    @GetMapping
    @Override
    public ResponseEntity<List<EmployeeResponseBean>> getAllEmployees() throws ServiceException, RateLimitException {
        log.info("Get All Employees API called.");
        return employeeService.getAllEmployees();
    }

    @GetMapping("/search/{searchString}")
    @Override
    public ResponseEntity<List<EmployeeResponseBean>> getEmployeesByNameSearch(String searchString)
            throws ServiceException, RateLimitException, NotFoundException {
        log.info("Search Employee by name API called.");
        return employeeService.getEmployeesByNameSearch(searchString);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<EmployeeResponseBean> getEmployeeById(String id)
            throws ServiceException, RateLimitException, NotFoundException {
        log.info("Get Employee by Id API called.");
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/highestSalary")
    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() throws ServiceException, RateLimitException {
        log.info("Get Highest salary of Employee API called.");
        return employeeService.getHighestSalaryOfEmployees();
    }

    @GetMapping("/topTenHighestEarningEmployeeNames")
    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames()
            throws ServiceException, RateLimitException {
        log.info("Get top 10 highest earning employees API called.");
        return employeeService.getTopTenHighestEarningEmployeeNames();
    }

    @PostMapping
    @Override
    public ResponseEntity<EmployeeResponseBean> createEmployee(EmployeeRequestBean employeeRequestBean)
            throws ServiceException, RateLimitException {
        log.info("Create Employee API called.");
        return employeeService.createEmployee(employeeRequestBean);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteEmployeeById(String id)
            throws ServiceException, RateLimitException, NotFoundException {
        log.info("Delete Employee by Id API called.");
        return employeeService.deleteEmployeeById(id);
    }
}
