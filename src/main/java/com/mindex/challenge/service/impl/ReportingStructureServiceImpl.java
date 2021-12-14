package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure getTotalReports(String id) {
        LOG.debug("Received request to calculate reporting for employee id [{}]", id);
        Employee employee = employeeService.read(id);
        if (employee == null) {
            throw new RuntimeException("Employee does not exist");
        }

        int totalReports = this.calculateDirectReports(employee);

        ReportingStructure reportingStructure = new ReportingStructure(employee, totalReports);;

        return reportingStructure;
    }

    public int calculateDirectReports(Employee emp){
        LOG.debug("Calculating direct reports");
        int totalReports = 0;

        //Initialize queue to keep track of all employees
        Queue<Employee> q = new LinkedList<>();
        q.add(emp);

        while (!q.isEmpty())
        {
            Employee currentEmployee = q.peek();
            q.remove();
            List<Employee> directReports = currentEmployee.getDirectReports();
            if(directReports != null){
                //loop through all direct reporting employees
                for( int i=0;i<directReports.size();i++)
                {
                    totalReports++;

                    //add all direct reporting employees in the queue
                    String directEmployeeId  = directReports.get(i).getEmployeeId();
                    q.add(employeeService.read(directEmployeeId));
                }
            }
        }
        return totalReports;
    }

}
