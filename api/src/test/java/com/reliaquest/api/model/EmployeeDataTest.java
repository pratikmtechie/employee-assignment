package com.reliaquest.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EmployeeDataTest {

    @Test
    void testGettersAndSetters() {
        EmployeeData employeeData = new EmployeeData();
        EmployeeResponseBean responseBean = new EmployeeResponseBean();

        employeeData.setData(responseBean);
        employeeData.setStatus("SUCCESS");

        assertThat(employeeData.getData()).isEqualTo(responseBean);
        assertThat(employeeData.getStatus()).isEqualTo("SUCCESS");
    }
}
