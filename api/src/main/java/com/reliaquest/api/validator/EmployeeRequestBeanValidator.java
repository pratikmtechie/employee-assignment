package com.reliaquest.api.validator;

import com.reliaquest.api.constants.ApplicationConstants;
import com.reliaquest.api.model.EmployeeRequestBean;
import java.util.List;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class EmployeeRequestBeanValidator {
    public Boolean validate(EmployeeRequestBean employeeRequestBean, List<String> errorCodes) {
        Boolean isValid = Boolean.TRUE;
        validateName(employeeRequestBean.getEmployeeName(), errorCodes);
        validateAge(employeeRequestBean.getEmployeeAge(), errorCodes);
        validateSalary(employeeRequestBean.getEmployeeSalary(), errorCodes);
        validateTitle(employeeRequestBean.getEmployeeTitle(), errorCodes);

        if (!CollectionUtils.isEmpty(errorCodes)) {
            isValid = Boolean.FALSE;
        }
        return isValid;
    }

    private void validateTitle(String title, List<String> errorCodes) {
        if (Strings.isBlank(title)) {
            errorCodes.add("EMP_TITLE_ERR");
        }
    }

    private void validateSalary(Integer salary, List<String> errorCodes) {
        if (salary < 0) {
            errorCodes.add("EMP_SALARY_ERR");
        }
    }

    private void validateAge(Integer age, List<String> errorCodes) {
        if (age < ApplicationConstants.MIN_AGE || age > ApplicationConstants.MAX_AGE) {
            errorCodes.add("EMP_AGE_ERR");
        }
    }

    private void validateName(String name, List<String> errorCodes) {
        if (Strings.isBlank(name)) {
            errorCodes.add("EMP_NAME_ERR");
        }
    }
}
