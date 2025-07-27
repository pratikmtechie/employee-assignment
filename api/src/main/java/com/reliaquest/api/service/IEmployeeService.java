package com.reliaquest.api.service;

import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.EmployeeRequestBean;
import com.reliaquest.api.model.EmployeeResponseBean;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IEmployeeService {

    ResponseEntity<List<EmployeeResponseBean>> getAllEmployees() throws ServiceException, RateLimitException;

    ResponseEntity<List<EmployeeResponseBean>> getEmployeesByNameSearch(String searchString)
            throws ServiceException, RateLimitException, NotFoundException;

    ResponseEntity<EmployeeResponseBean> getEmployeeById(String id)
            throws ServiceException, RateLimitException, NotFoundException;

    ResponseEntity<Integer> getHighestSalaryOfEmployees() throws ServiceException, RateLimitException;

    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() throws ServiceException, RateLimitException;

    ResponseEntity<EmployeeResponseBean> createEmployee(EmployeeRequestBean employeeInput)
            throws ServiceException, RateLimitException;

    ResponseEntity<String> deleteEmployeeById(String id) throws ServiceException, RateLimitException, NotFoundException;
}
