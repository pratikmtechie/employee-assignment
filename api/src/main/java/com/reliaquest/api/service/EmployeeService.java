package com.reliaquest.api.service;

import com.reliaquest.api.client.IEmpolyeeClient;
import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.*;
import com.reliaquest.api.validator.EmployeeRequestBeanValidator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Log4j2
@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    private IEmpolyeeClient employeeClient;

    @Autowired
    private EmployeeRequestBeanValidator employeeRequestBeanValidator;

    @Override
    public ResponseEntity<List<EmployeeResponseBean>> getAllEmployees() throws ServiceException, RateLimitException {
        log.info("getAllEmployees method execution started");
        List<EmployeeResponseBean> employees = employeeClient.getAllEmployees();
        if (CollectionUtils.isEmpty(employees)) {
            log.error("No employees are present in the system.");
            throw new ServiceException(List.of("EMP_LIST_EMPTY"));
        }
        log.info("getAllEmployees method execution completed");
        return ResponseEntity.ok(employeeClient.getAllEmployees());
    }

    @Override
    public ResponseEntity<List<EmployeeResponseBean>> getEmployeesByNameSearch(String searchString)
            throws ServiceException, RateLimitException, NotFoundException {
        log.info("getEmployeesByNameSearch method execution started");
        List<EmployeeResponseBean> allEmployees = employeeClient.getAllEmployees();
        List<EmployeeResponseBean> matchingEmployees = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allEmployees)) {
            matchingEmployees = allEmployees.stream()
                    .filter(e -> searchString.equalsIgnoreCase(e.getEmployeeName())
                            || e.getEmployeeName().toLowerCase().contains(searchString.toLowerCase()))
                    .toList();
        }
        if (CollectionUtils.isEmpty(matchingEmployees)) {
            log.error("No matching employees found for the provided name : {}", searchString);
            throw new NotFoundException(List.of("EMP_NAME_NOT_PRESENT"));
        }
        log.info("getEmployeesByNameSearch method execution completed");
        return ResponseEntity.ok(matchingEmployees);
    }

    @Override
    public ResponseEntity<EmployeeResponseBean> getEmployeeById(String id)
            throws ServiceException, RateLimitException, NotFoundException {
        log.info("getEmployeeById method execution started");
        EmployeeResponseBean employee = employeeClient.getEmployeeById(id);
        log.info("getEmployeeById method execution completed");
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() throws ServiceException, RateLimitException {
        log.info("getHighestSalaryOfEmployees method execution started");
        List<EmployeeResponseBean> allEmployees = employeeClient.getAllEmployees();
        if (CollectionUtils.isEmpty(allEmployees)) {
            log.error("No employees are present in the system.");
            throw new ServiceException(List.of("EMP_LIST_EMPTY"));
        }
        log.info("getHighestSalaryOfEmployees method execution completed");
        return ResponseEntity.ok(allEmployees.stream()
                .map(EmployeeResponseBean::getEmployeeSalary)
                .max(Integer::compareTo)
                .get());
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames()
            throws ServiceException, RateLimitException {
        log.info("getTopTenHighestEarningEmployeeNames method execution started");
        List<EmployeeResponseBean> allEmployees = employeeClient.getAllEmployees();
        if (CollectionUtils.isEmpty(allEmployees)) {
            log.error("No employees are present in the system.");
            throw new ServiceException(List.of("EMP_LIST_EMPTY"));
        }
        log.info("getTopTenHighestEarningEmployeeNames method execution completed");
        return ResponseEntity.ok(allEmployees.stream()
                .sorted(Comparator.comparingInt(EmployeeResponseBean::getEmployeeSalary)
                        .reversed())
                .limit(10)
                .map(EmployeeResponseBean::getEmployeeName)
                .toList());
    }

    @Override
    public ResponseEntity<EmployeeResponseBean> createEmployee(EmployeeRequestBean employeeRequestBean)
            throws ServiceException, RateLimitException {
        log.info("createEmployee method execution started");
        List<String> errorCodes = new ArrayList<>();
        Boolean isRequestValid = employeeRequestBeanValidator.validate(employeeRequestBean, errorCodes);
        if (!isRequestValid) {
            throw new ServiceException(errorCodes);
        }
        EmployeeResponseBean employeeResponseBean =
                employeeClient.createEmployee(getCreateEmployeeEntity(employeeRequestBean));
        if (Objects.isNull(employeeResponseBean)) {
            log.error("Failed to create Employee in the system.");
            throw new ServiceException(List.of("EMP_CREATE_FAILED"));
        }
        log.info("createEmployee method execution completed");
        return ResponseEntity.ok(employeeResponseBean);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id)
            throws ServiceException, RateLimitException, NotFoundException {
        log.info("deleteEmployeeById method execution started");
        EmployeeResponseBean employeeResponseBean = employeeClient.getEmployeeById(id);
        EmployeeDeleteRequest employeeDeleteRequest = new EmployeeDeleteRequest(employeeResponseBean.getEmployeeName());
        EmployeeDeleteResponse employeeDeleteResponse = employeeClient.deleteEmployeeById(employeeDeleteRequest);
        if (Objects.isNull(employeeResponseBean)) {
            log.error("Delete operation failed for employee id : {}", id);
            throw new ServiceException(List.of("EMP_DELETE_FAILED"));
        }
        log.info("deleteEmployeeById method execution completed");
        return ResponseEntity.ok(employeeResponseBean.getEmployeeName());
    }

    private EmployeeCreateRequest getCreateEmployeeEntity(EmployeeRequestBean employeeRequestBean) {
        return EmployeeCreateRequest.builder()
                .name(employeeRequestBean.getEmployeeName())
                .age(employeeRequestBean.getEmployeeAge())
                .salary(employeeRequestBean.getEmployeeSalary())
                .title(employeeRequestBean.getEmployeeTitle())
                .build();
    }
}
