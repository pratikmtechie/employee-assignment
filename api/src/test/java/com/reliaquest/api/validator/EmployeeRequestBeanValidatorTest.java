package com.reliaquest.api.validator;

import static org.junit.jupiter.api.Assertions.*;

import com.reliaquest.api.model.EmployeeRequestBean;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeRequestBeanValidatorTest {

    private EmployeeRequestBeanValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmployeeRequestBeanValidator();
    }

    @Test
    void testValidEmployeeRequest() {
        EmployeeRequestBean bean = new EmployeeRequestBean();
        bean.setEmployeeName("John Doe");
        bean.setEmployeeAge(30);
        bean.setEmployeeSalary(50000);
        bean.setEmployeeTitle("Developer");

        List<String> errors = new ArrayList<>();
        Boolean isValid = validator.validate(bean, errors);

        assertTrue(isValid);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testInvalidEmployeeName() {
        EmployeeRequestBean bean = new EmployeeRequestBean();
        bean.setEmployeeName("   ");
        bean.setEmployeeAge(30);
        bean.setEmployeeSalary(50000);
        bean.setEmployeeTitle("Developer");

        List<String> errors = new ArrayList<>();
        Boolean isValid = validator.validate(bean, errors);

        assertFalse(isValid);
        assertTrue(errors.contains("EMP_NAME_ERR"));
    }

    @Test
    void testInvalidEmployeeAge() {
        EmployeeRequestBean bean = new EmployeeRequestBean();
        bean.setEmployeeName("Jane");
        bean.setEmployeeAge(10);
        bean.setEmployeeSalary(50000);
        bean.setEmployeeTitle("Analyst");

        List<String> errors = new ArrayList<>();
        Boolean isValid = validator.validate(bean, errors);

        assertFalse(isValid);
        assertTrue(errors.contains("EMP_AGE_ERR"));
    }

    @Test
    void testInvalidEmployeeSalary() {
        EmployeeRequestBean bean = new EmployeeRequestBean();
        bean.setEmployeeName("Mike");
        bean.setEmployeeAge(25);
        bean.setEmployeeSalary(-1000);
        bean.setEmployeeTitle("Tester");

        List<String> errors = new ArrayList<>();
        Boolean isValid = validator.validate(bean, errors);

        assertFalse(isValid);
        assertTrue(errors.contains("EMP_SALARY_ERR"));
    }

    @Test
    void testInvalidEmployeeTitle() {
        EmployeeRequestBean bean = new EmployeeRequestBean();
        bean.setEmployeeName("Anna");
        bean.setEmployeeAge(35);
        bean.setEmployeeSalary(75000);
        bean.setEmployeeTitle("");

        List<String> errors = new ArrayList<>();
        Boolean isValid = validator.validate(bean, errors);

        assertFalse(isValid);
        assertTrue(errors.contains("EMP_TITLE_ERR"));
    }

    @Test
    void testMultipleErrors() {
        EmployeeRequestBean bean = new EmployeeRequestBean();
        bean.setEmployeeName("");
        bean.setEmployeeAge(5);
        bean.setEmployeeSalary(-100);
        bean.setEmployeeTitle("   ");

        List<String> errors = new ArrayList<>();
        Boolean isValid = validator.validate(bean, errors);

        assertFalse(isValid);
        assertEquals(4, errors.size());
        assertTrue(errors.contains("EMP_NAME_ERR"));
        assertTrue(errors.contains("EMP_AGE_ERR"));
        assertTrue(errors.contains("EMP_SALARY_ERR"));
        assertTrue(errors.contains("EMP_TITLE_ERR"));
    }
}
