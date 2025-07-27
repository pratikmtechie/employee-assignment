package com.reliaquest.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class EmployeesDataTest {

    @Test
    void testGettersAndSetters() {
        EmployeesData employeesData = new EmployeesData();

        EmployeeResponseBean employee1 = new EmployeeResponseBean();
        employee1.setEmployeeName("Alice");
        EmployeeResponseBean employee2 = new EmployeeResponseBean();
        employee2.setEmployeeName("Bob");

        List<EmployeeResponseBean> employeeList = List.of(employee1, employee2);
        String status = "success";

        employeesData.setData(employeeList);
        employeesData.setStatus(status);

        assertThat(employeesData.getData())
                .hasSize(2)
                .extracting(EmployeeResponseBean::getEmployeeName)
                .containsExactly("Alice", "Bob");
        assertThat(employeesData.getStatus()).isEqualTo("success");
    }
}
