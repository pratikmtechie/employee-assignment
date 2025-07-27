package com.reliaquest.api.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreateRequest {
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
}
