package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);
        
        if (compensation.getEmployee() == null || compensation.getEmployee().getEmployeeId() == null) {
            throw new RuntimeException("Cannot create compensation record without employee id");
        }
        /*
        **Additional null / invalid value checks for effectiveDate and salary if described in the task description
        */
        // Check if the employee exists - will throw an error if an employee record doesn't exist
        employeService.read(compensation.getEmployee().getEmployeeId());
        Compensation comp = compensationRepository.findByEmployee_EmployeeId(compensation.getEmployee().getEmployeeId());
        // This is to stop multiple compensation records for a single employee as I think it should be 1 to 1
        if (comp != null) {
            throw new RuntimeException("Compensation record already exists for employee with id:" + compensation.getEmployee().getEmployeeId());
        }
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Creating compensation with employee id [{}]", id);

        Compensation compensation = compensationRepository.findByEmployee_EmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("No compensation record found for id: " + id);
        }

        return compensation;
    }
}
