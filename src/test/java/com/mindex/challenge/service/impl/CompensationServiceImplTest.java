package com.mindex.challenge.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private String createCompensationUrl;
    private String readCompensationUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Compensation testOne;
    private Compensation testTwo;

    @Before
    public void setup() {
        createCompensationUrl = "http://localhost:" + port + "/compensation";
        readCompensationUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateReadUpdate() throws ParseException {
        //Unit test one
        Employee employeeOne = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");
        String sDate1="2017-10-05";
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        testOne = new Compensation(employeeOne, "50000", date1);

        // Create checks
        ResponseEntity createdResponse = restTemplate.postForEntity(createCompensationUrl, testOne, Compensation.class);
        assertEquals(HttpStatus.OK, createdResponse.getStatusCode());
        Compensation createdCompensationOne = (Compensation)createdResponse.getBody();
        assertNotNull(createdCompensationOne);
        assertCompensationEquivalence(testOne, createdCompensationOne);

        // Read checks
        ResponseEntity readResponse = restTemplate.getForEntity(readCompensationUrl, Compensation.class, createdCompensationOne.getEmployee().getEmployeeId());
        assertEquals(HttpStatus.OK, readResponse.getStatusCode());
        Compensation readCompensation = (Compensation)readResponse.getBody();
        assertNotNull(readCompensation);
        assertCompensationEquivalence(createdCompensationOne, readCompensation);
    }

    private void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getSalary(),actual.getSalary());
        assertEquals(expected.getEffectiveDate(),actual.getEffectiveDate());
        Employee expectedEmployee = expected.getEmployee();
        Employee actualEmployee = actual.getEmployee();

        assertEquals(expectedEmployee.getFirstName(), actualEmployee.getFirstName());
        assertEquals(expectedEmployee.getLastName(), actualEmployee.getLastName());
        assertEquals(expectedEmployee.getDepartment(), actualEmployee.getDepartment());
        assertEquals(expectedEmployee.getPosition(), actualEmployee.getPosition());
    }
}
