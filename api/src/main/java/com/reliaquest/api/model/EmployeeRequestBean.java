package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestBean {

    @NotNull(message = "Employee Name cannot be null.") @JsonAlias("employee_name")
    private String employeeName;

    @Size(min = 0, message = "Salary cannot be less than ")
    @JsonAlias("employee_salary")
    private Integer employeeSalary;

    @Min(value = 16, message = "Employee age cannot be less than 16.")
    @Max(value = 75, message = "Employee age cannot be greater than 75.")
    @JsonAlias("employee_age")
    private Integer employeeAge;

    @NotNull(message = "Employee Title cannot be null.") @JsonAlias("employee_title")
    private String employeeTitle;

    @Email(message = "Please provide a valid email address")
    @JsonAlias("employee_email")
    private String employeeEmail;
}
