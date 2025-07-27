package com.reliaquest.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EmployeeDeleteResponseTest {

    @Test
    void testGettersAndSetters() {
        EmployeeDeleteResponse response = new EmployeeDeleteResponse();

        response.setData(true);
        response.setStatus("SUCCESS");

        assertThat(response.getData()).isTrue();
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
    }
}
