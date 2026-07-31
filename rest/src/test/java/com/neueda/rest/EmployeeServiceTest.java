package com.neueda.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmpRepository empRepo;

    @InjectMocks
    EmpService empService;

    @Test
    void shouldReturnEmployees()
    {
        List<Employee> list=List.of(
                new Employee("John","IT",50000),
                new Employee("David","HR",60000)
        );

        when(empRepo.getAllEmployees()).thenReturn(list);
        List<Employee> result=
                empService.getAllEmployees();

        assertEquals(2,result.size());
        verify(empRepo).getAllEmployees();
    }

    @Test
    void shouldReturnEmptyEmployeeListWhenRepositoryHasNoEmployees()
    {
        when(empRepo.getAllEmployees()).thenReturn(List.of());

        List<Employee> result=empService.getAllEmployees();

        assertTrue(result.isEmpty());
        verify(empRepo).getAllEmployees();
    }

    @Test
    void shouldAddEmployee()
    {
        Employee employee=new Employee("Alice","Finance",70000);

        empService.addEmployee(employee);

        verify(empRepo).addEmployee(employee);
    }

    @Test
    void shouldDeleteEmployeeById()
    {
        int id=10;

        empService.deleteEmployee(id);

        verify(empRepo).deleteEmployee(id);
    }

    @Test
    void shouldUpdateEmployeeById()
    {
        int id=5;
        Employee employee=new Employee("Maya","Marketing",65000);

        empService.updateEmployee(id,employee);

        verify(empRepo).updateEmployee(id,employee);
    }

    @Test
    void shouldReturnEmployeeByIdWhenEmployeeExists()
    {
        Employee employee=new Employee("John","IT",50000);
        when(empRepo.getEmployeeById(1)).thenReturn(employee);

        Employee result=empService.getEmployeeById(1);

        assertEquals("John",result.getName());
        assertEquals("IT",result.getDepartment());
        assertEquals(50000,result.getSalary());
        verify(empRepo).getEmployeeById(1);
    }

    @Test
    void shouldThrowEmployeeNotFoundWhenEmployeeDoesNotExist()
    {
        when(empRepo.getEmployeeById(99)).thenReturn(null);

        EmployeeNotFoundException ex=assertThrows(EmployeeNotFoundException.class,
                () -> empService.getEmployeeById(99));

        assertTrue(ex.getMessage().contains("99"));
        verify(empRepo).getEmployeeById(99);
    }

    @Test
    void shouldPropagateEmployeeNotFoundExceptionFromRepository()
    {
        when(empRepo.getEmployeeById(7))
                .thenThrow(new EmployeeNotFoundException("Employee with id 7 not found"));

        EmployeeNotFoundException ex=assertThrows(EmployeeNotFoundException.class,
                () -> empService.getEmployeeById(7));

        assertTrue(ex.getMessage().contains("7"));
        verify(empRepo).getEmployeeById(7);
    }



}
