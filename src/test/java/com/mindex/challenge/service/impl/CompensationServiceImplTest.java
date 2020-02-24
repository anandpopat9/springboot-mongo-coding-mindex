package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private String employeeUrl;
    private String postCompensationUrl;
    private String getCompensationUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee/{id}";
        postCompensationUrl = "http://localhost:" + port + "/compensation";
        getCompensationUrl = "http://localhost:" + port + "/compensation/employee/{id}";
    }

    @Test
    public void testCreateReadCompensation() {
        Compensation testCompensation = new Compensation();
        Employee testEmployee = restTemplate.getForEntity(employeeUrl, Employee.class, "16a596ae-edd3-4847-99fe-c4518e82c86f").getBody();
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(1000);
        testCompensation.setEffectiveDate(new Date());

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(postCompensationUrl, testCompensation, Compensation.class).getBody();
        assertNotNull(createdCompensation.getEmployee());
        assertCompensationEquivalence(testCompensation, createdCompensation);


        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(getCompensationUrl, Compensation.class, testEmployee.getEmployeeId()).getBody();
        assertEquals(createdCompensation.getEmployee().getEmployeeId(), readCompensation.getEmployee().getEmployeeId());
        assertCompensationEquivalence(createdCompensation, readCompensation);

        // Check for all the invalid types
        // Creating compensation with the same employee 
        createdCompensation = restTemplate.postForEntity(postCompensationUrl, testCompensation, Compensation.class).getBody();
        // Assert null to indicate failure - can use AssertThrows JUnit 4/5
        assertNull(createdCompensation.getEmployee());

        // Employee id null
        testCompensation.getEmployee().setEmployeeId(null);
        createdCompensation = restTemplate.postForEntity(postCompensationUrl, testCompensation, Compensation.class).getBody();
        assertNull(createdCompensation.getEmployee());

        // Invalid employee id
        testCompensation.getEmployee().setEmployeeId("abc");
        createdCompensation = restTemplate.postForEntity(postCompensationUrl, testCompensation, Compensation.class).getBody();
        assertNull(createdCompensation.getEmployee());

        // Employee is null
        testCompensation.setEmployee(null);
        createdCompensation = restTemplate.postForEntity(postCompensationUrl, testCompensation, Compensation.class).getBody();
        assertNull(createdCompensation.getEmployee());
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
