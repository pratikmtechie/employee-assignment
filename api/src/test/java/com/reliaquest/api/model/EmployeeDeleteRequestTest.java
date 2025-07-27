package com.reliaquest.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EmployeeDeleteRequestTest {

    @Test
    void testConstructorAndGetter() {
        String name = "John Doe";
        EmployeeDeleteRequest request = new EmployeeDeleteRequest(name);
        assertThat(request.getName()).isEqualTo(name);
    }
}
