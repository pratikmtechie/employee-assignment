package com.reliaquest.api.client;

import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import com.reliaquest.api.model.EmployeeCreateRequest;
import com.reliaquest.api.model.EmployeeDeleteRequest;
import com.reliaquest.api.model.EmployeeDeleteResponse;
import com.reliaquest.api.model.EmployeeResponseBean;
import java.util.List;

public interface IEmpolyeeClient {

    List<EmployeeResponseBean> getAllEmployees() throws ServiceException, RateLimitException;

    EmployeeResponseBean getEmployeeById(String id) throws ServiceException, RateLimitException, NotFoundException;

    EmployeeResponseBean createEmployee(EmployeeCreateRequest createEmployeeEntity)
            throws ServiceException, RateLimitException;

    EmployeeDeleteResponse deleteEmployeeById(EmployeeDeleteRequest employeeDeleteRequest)
            throws ServiceException, RateLimitException;
}
