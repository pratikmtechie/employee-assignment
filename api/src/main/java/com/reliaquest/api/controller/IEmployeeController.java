package com.reliaquest.api.controller;

import com.reliaquest.api.exception.NotFoundException;
import com.reliaquest.api.exception.RateLimitException;
import com.reliaquest.api.exception.ServiceException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Please <b>do not</b> modify this interface. If you believe there's a bug or the API contract does not align with our
 * mock web server... that is intentional. Good luck!
 *
 * @implNote It's uncommon to have a web controller implement an interface; We include such design pattern to
 * ensure users are following the desired input/output for our API contract, as outlined in the code assessment's README.
 *
 * @param <Entity> object representation of an Employee
 * @param <Input> object representation of a request body for creating Employee(s)
 */
public interface IEmployeeController<Entity, Input> {

    @GetMapping()
    ResponseEntity<List<Entity>> getAllEmployees() throws ServiceException, RateLimitException;

    @GetMapping("/search/{searchString}")
    ResponseEntity<List<Entity>> getEmployeesByNameSearch(@PathVariable String searchString)
            throws ServiceException, RateLimitException, NotFoundException;

    @GetMapping("/{id}")
    ResponseEntity<Entity> getEmployeeById(@PathVariable String id)
            throws ServiceException, RateLimitException, NotFoundException;

    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees() throws ServiceException, RateLimitException;

    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() throws ServiceException, RateLimitException;

    @PostMapping()
    ResponseEntity<Entity> createEmployee(@RequestBody Input employeeInput) throws ServiceException, RateLimitException;

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id)
            throws ServiceException, RateLimitException, NotFoundException;
}
