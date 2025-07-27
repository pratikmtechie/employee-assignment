package com.reliaquest.api.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeesData {
    private List<EmployeeResponseBean> data;
    private String status;
}
