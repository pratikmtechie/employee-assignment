package com.reliaquest.api.client;

import com.reliaquest.api.constants.ApplicationConstants;
import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.*;
import java.util.List;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Component
public class EmployeeClient implements IEmpolyeeClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${employee.client.base.url}")
    private String empClientBaseUrl;

    @Override
    public List<EmployeeResponseBean> getAllEmployees() throws ServiceException, RateLimitException {
        ResponseEntity<EmployeesData> employeesResponseEntity;

        try {
            employeesResponseEntity = restTemplate.exchange(
                    empClientBaseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.error("Too many requests received by Get All Employees API");
            throw new RateLimitException(List.of("RATE_LIMIT_ERR"));
        } catch (RestClientException e) {
            log.error("Exception occurred while fetching all employees. Exception is : {}", e.getMessage());
            throw new ServiceException(List.of("INT_SER_ERR"));
        }

        if (!HttpStatus.OK.equals(employeesResponseEntity.getStatusCode())
                || Objects.isNull(employeesResponseEntity.getBody())) {
            log.error("Get all employees API call failed.");
            throw new ServiceException(List.of("INT_SER_ERR"));
        }
        return employeesResponseEntity.getBody().getData();
    }

    @Override
    public EmployeeResponseBean getEmployeeById(String id)
            throws ServiceException, RateLimitException, NotFoundException {
        ResponseEntity<EmployeeData> employeeResponseEntity;

        try {
            employeeResponseEntity = restTemplate.getForEntity(getFullUrl(id), EmployeeData.class);
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.error("Too many requests received by Get Employees by Id API");
            throw new RateLimitException(List.of("RATE_LIMIT_ERR"));
        } catch (HttpClientErrorException.NotFound e) {
            log.error("No matching employee found for the provided id : {}", id);
            throw new NotFoundException(List.of("EMP_ID_NOT_PRESENT"));
        } catch (RestClientException e) {
            log.error(
                    "Exception occurred while fetching employee details for ID : {}, Exception is : {}",
                    id,
                    e.getMessage());
            throw new ServiceException(List.of("INT_SER_ERR"));
        }

        if (!HttpStatus.OK.equals(employeeResponseEntity.getStatusCode())
                || Objects.isNull(employeeResponseEntity.getBody())) {
            log.error("Failed to fetch employee details for ID : {}", id);
            throw new ServiceException(List.of("INT_SER_ERR"));
        }
        return employeeResponseEntity.getBody().getData();
    }

    @Override
    public EmployeeResponseBean createEmployee(EmployeeCreateRequest createEmployeeEntity)
            throws ServiceException, RateLimitException {
        ResponseEntity<EmployeeData> employeeResponseEntity;

        try {
            employeeResponseEntity =
                    restTemplate.postForEntity(empClientBaseUrl, createEmployeeEntity, EmployeeData.class);
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.error("Too many requests received by Create Employees API");
            throw new RateLimitException(List.of("RATE_LIMIT_ERR"));
        } catch (RestClientException e) {
            log.error(
                    "Exception occurred while posting employee details for : {}, Exception is : {}",
                    createEmployeeEntity.getName(),
                    e.getMessage());
            throw new ServiceException(List.of("INT_SER_ERR"));
        }

        if (!HttpStatus.OK.equals(employeeResponseEntity.getStatusCode())
                || Objects.isNull(employeeResponseEntity.getBody())) {
            log.error("Failed to post employee details for : {}", createEmployeeEntity.getName());
            throw new ServiceException(List.of("INT_SER_ERR"));
        }
        return employeeResponseEntity.getBody().getData();
    }

    @Override
    public EmployeeDeleteResponse deleteEmployeeById(EmployeeDeleteRequest employeeDeleteRequest)
            throws ServiceException, RateLimitException {
        ResponseEntity<EmployeeDeleteResponse> employeeResponseEntity;

        try {
            employeeResponseEntity = restTemplate.exchange(
                    empClientBaseUrl,
                    HttpMethod.DELETE,
                    new HttpEntity<>(employeeDeleteRequest, null),
                    new ParameterizedTypeReference<>() {});
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.error("Too many requests received by Delete Employees by Id API");
            throw new RateLimitException(List.of("RATE_LIMIT_ERR"));
        } catch (RestClientException e) {
            log.error(
                    "Exception occurred while deleting employee details for : {}, Exception is : {}",
                    employeeDeleteRequest.getName(),
                    e.getMessage());
            throw new ServiceException(List.of("INT_SER_ERR"));
        }

        if (!HttpStatus.OK.equals(employeeResponseEntity.getStatusCode())) {
            log.error("Failed to delete details for employee : {}", employeeDeleteRequest.getName());
            throw new ServiceException(List.of("INT_SER_ERR"));
        }

        return employeeResponseEntity.getBody();
    }

    private String getFullUrl(String employeeId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(empClientBaseUrl)
                .path(ApplicationConstants.SLASH)
                .path(employeeId);
        log.info("Generated URL : {}", uriBuilder.build().encode().toString());
        return uriBuilder.build().encode().toString();
    }
}
