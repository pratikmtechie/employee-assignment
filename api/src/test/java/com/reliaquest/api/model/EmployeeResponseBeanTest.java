package com.reliaquest.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EmployeeResponseBeanTest {

    @Test
    void testGettersAndSetters() {
        EmployeeResponseBean bean = new EmployeeResponseBean();

        bean.setId("123");
        bean.setEmployeeName("John Doe");
        bean.setEmployeeSalary(75000);
        bean.setEmployeeAge(35);
        bean.setEmployeeTitle("Manager");
        bean.setEmployeeEmail("john.doe@example.com");

        assertThat(bean.getId()).isEqualTo("123");
        assertThat(bean.getEmployeeName()).isEqualTo("John Doe");
        assertThat(bean.getEmployeeSalary()).isEqualTo(75000);
        assertThat(bean.getEmployeeAge()).isEqualTo(35);
        assertThat(bean.getEmployeeTitle()).isEqualTo("Manager");
        assertThat(bean.getEmployeeEmail()).isEqualTo("john.doe@example.com");
    }
}
