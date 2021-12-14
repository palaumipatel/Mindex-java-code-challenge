package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReportingStructureService resportStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Employee testOne;
    private Employee testTwo;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        //Unit test one
        testOne = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");
        ReportingStructure testReportingStructure = new ReportingStructure(testOne, 4);

        // Read checks
        ReportingStructure readReportingStructureResponse = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testOne).getBody();
        assertEquals(testReportingStructure.getNumberOfReports(), readReportingStructureResponse.getNumberOfReports());
        assertReportingEquivalence(testReportingStructure, readReportingStructureResponse);

        //Unit test two
        testTwo = employeeService.read("03aa1462-ffa9-4978-901b-7c001562cf6f");
        ReportingStructure testReportingStructureTwo = new ReportingStructure(testTwo, 2);

        ReportingStructure readReportingStructureResponseTwo = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, testTwo).getBody();
        assertEquals(testReportingStructureTwo.getNumberOfReports(), readReportingStructureResponseTwo.getNumberOfReports());
        assertReportingEquivalence(testReportingStructureTwo, readReportingStructureResponseTwo);
    }

    private static void assertReportingEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
        Employee expectedEmployee = expected.getEmployee();
        Employee actualEmployee = actual.getEmployee();

        assertEquals(expectedEmployee.getFirstName(), actualEmployee.getFirstName());
        assertEquals(expectedEmployee.getLastName(), actualEmployee.getLastName());
        assertEquals(expectedEmployee.getDepartment(), actualEmployee.getDepartment());
        assertEquals(expectedEmployee.getPosition(), actualEmployee.getPosition());
    }
}
