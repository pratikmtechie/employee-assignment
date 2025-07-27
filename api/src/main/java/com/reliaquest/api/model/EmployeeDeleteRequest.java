package com.reliaquest.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDeleteRequest {
    private String name;

    public EmployeeDeleteRequest(String name) {
        this.name = name;
    }
}
