package com.reliaquest.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EmployeeCreateRequestTest {

    @Test
    void testGettersAndSetters() {
        EmployeeCreateRequest request = new EmployeeCreateRequest();

        request.setName("John Doe");
        request.setSalary(50000);
        request.setAge(30);
        request.setTitle("Developer");

        assertThat(request.getName()).isEqualTo("John Doe");
        assertThat(request.getSalary()).isEqualTo(50000);
        assertThat(request.getAge()).isEqualTo(30);
        assertThat(request.getTitle()).isEqualTo("Developer");
    }
}
