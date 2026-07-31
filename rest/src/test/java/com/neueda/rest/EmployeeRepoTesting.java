package com.neueda.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@Import(EmpRepository.class)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepoTesting {

    @Autowired
    EmpRepository empRepo;

    @Test
    void shouldReturnEmployees()
    {
        List<Employee> employees= empRepo.getAllEmployees();

        assertFalse(employees.isEmpty());
    }

    @Test
    void shouldAddEmployeeAndRetrieveById()
    {
        String uniqueName="RepoAdd-"+UUID.randomUUID();
        Employee employee=new Employee(uniqueName,"QA",45000);

        empRepo.addEmployee(employee);

        Optional<Employee> inserted=empRepo.getAllEmployees().stream()
                .filter(emp -> uniqueName.equals(emp.getName()))
                .findFirst();
        assertTrue(inserted.isPresent());

        Employee fetched=empRepo.getEmployeeById(inserted.get().getId());
        assertEquals(uniqueName,fetched.getName());
        assertEquals("QA",fetched.getDepartment());
        assertEquals(45000,fetched.getSalary());
    }

    @Test
    void shouldUpdateEmployeeDetailsWhenEmployeeExists()
    {
        String originalName="RepoUpdate-"+UUID.randomUUID();
        empRepo.addEmployee(new Employee(originalName,"Support",38000));

        Employee inserted=empRepo.getAllEmployees().stream()
                .filter(emp -> originalName.equals(emp.getName()))
                .findFirst()
                .orElseThrow();

        Employee updatedDetails=new Employee("RepoUpdated-"+UUID.randomUUID(),"Engineering",52000);
        empRepo.updateEmployee(inserted.getId(),updatedDetails);

        Employee updated=empRepo.getEmployeeById(inserted.getId());
        assertEquals(updatedDetails.getName(),updated.getName());
        assertEquals(updatedDetails.getDepartment(),updated.getDepartment());
        assertEquals(updatedDetails.getSalary(),updated.getSalary());
    }

    @Test
    void shouldDeleteEmployeeWhenEmployeeExists()
    {
        String uniqueName="RepoDelete-"+UUID.randomUUID();
        empRepo.addEmployee(new Employee(uniqueName,"Ops",41000));

        Employee inserted=empRepo.getAllEmployees().stream()
                .filter(emp -> uniqueName.equals(emp.getName()))
                .findFirst()
                .orElseThrow();

        empRepo.deleteEmployee(inserted.getId());

        assertThrows(EmployeeNotFoundException.class,() -> empRepo.getEmployeeById(inserted.getId()));
    }

    @Test
    void shouldThrowEmployeeNotFoundWhenIdDoesNotExist()
    {
        assertThrows(EmployeeNotFoundException.class,() -> empRepo.getEmployeeById(Integer.MIN_VALUE));
    }

    @Test
    void shouldKeepExistingEmployeeWhenDeletingUnknownId()
    {
        String uniqueName="RepoKeepOnDelete-"+UUID.randomUUID();
        empRepo.addEmployee(new Employee(uniqueName,"Admin",39000));

        Employee inserted=empRepo.getAllEmployees().stream()
                .filter(emp -> uniqueName.equals(emp.getName()))
                .findFirst()
                .orElseThrow();

        empRepo.deleteEmployee(Integer.MIN_VALUE);

        Employee stillPresent=empRepo.getEmployeeById(inserted.getId());
        assertNotNull(stillPresent);
        assertEquals(uniqueName,stillPresent.getName());
    }

    @Test
    void shouldKeepExistingEmployeeUnchangedWhenUpdatingUnknownId()
    {
        String uniqueName="RepoKeepOnUpdate-"+UUID.randomUUID();
        empRepo.addEmployee(new Employee(uniqueName,"Legal",47000));

        Employee inserted=empRepo.getAllEmployees().stream()
                .filter(emp -> uniqueName.equals(emp.getName()))
                .findFirst()
                .orElseThrow();

        Employee unknownUpdate=new Employee("ShouldNotApply-"+UUID.randomUUID(),"Other",99999);
        empRepo.updateEmployee(Integer.MIN_VALUE,unknownUpdate);

        Employee stillPresent=empRepo.getEmployeeById(inserted.getId());
        assertEquals(uniqueName,stillPresent.getName());
        assertEquals("Legal",stillPresent.getDepartment());
        assertEquals(47000,stillPresent.getSalary());
    }

}
