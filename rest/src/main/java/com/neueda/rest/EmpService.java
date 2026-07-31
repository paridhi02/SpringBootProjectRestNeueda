package com.neueda.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpService {
    @Autowired
    private EmpRepository empRepo;

    public List<Employee> getAllEmployees()
    {
        return empRepo.getAllEmployees();
    }

    public void addEmployee(Employee emp)
    {
        empRepo.addEmployee(emp);
    }

    public void deleteEmployee(int id)
    {
       empRepo.deleteEmployee(id);
    }
      public void updateEmployee(int id,Employee emp)
      {
          empRepo.updateEmployee(id,emp);
      }
      public Employee getEmployeeById(int id)
      {
          Employee emp= empRepo.getEmployeeById(id);
          if(emp==null)
          {
              throw new EmployeeNotFoundException("Employee with id "+id +"not found");
          }
          return emp;
      }




}
