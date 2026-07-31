package com.neueda.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Repository
public class EmpRepository {

  @Autowired
    private JdbcTemplate jdbcTemplate;
  public List<Employee> getAllEmployees()
  {
      String sql="SELECT * FROM employee";
      return jdbcTemplate.query(
              sql,new BeanPropertyRowMapper<>(Employee.class)
      );
  }

  public void addEmployee(Employee emp)
  {
      String sql="INSERT INTO employee(name,department,salary) VALUES (?,?,?)";
      jdbcTemplate.update(sql,
              emp.getName(),
              emp.getDepartment(),
              emp.getSalary());
  }

  public void deleteEmployee(int id)
  {

      String sql="DELETE FROM employee WHERE id=?";
      jdbcTemplate.update(sql,id);

  }

  public void updateEmployee(int id,Employee emp)
  {
      String sql="UPDATE employee SET name=?,department=?,salary=? WHERE id=?";
      jdbcTemplate.update(sql,
              emp.getName(),
              emp.getDepartment(),
              emp.getSalary(),
              id
      );
  }

  public Employee getEmployeeById(int id)
  {

      String sql="SELECT * FROM employee WHERE id=?";
      try {
          return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class), id);
      }
      catch(EmptyResultDataAccessException ex)
      {
          throw new EmployeeNotFoundException("Employee with id "+id+" not found");

  }
  }



}
