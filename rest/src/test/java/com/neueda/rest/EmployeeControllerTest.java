package com.neueda.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmpController.class)
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmpService empService;

    @Test
    void shouldReturnEmployees() throws Exception{

        List<Employee> list=List.of(
                new Employee("John","IT",50000)
        );


        when(empService.getAllEmployees()).
                thenReturn(list);


        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Employess: '][0].name").value("John"));
    }

    @Test
    void shouldReturnEmptyEmployeesWhenNoEmployeesExist() throws Exception {
        when(empService.getAllEmployees()).thenReturn(List.of());

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Employess: ']").isArray())
                .andExpect(jsonPath("$['Employess: ']").isEmpty());
    }

    @Test
    void shouldReturnEmployeeByIdWhenEmployeeExists() throws Exception {
        Employee employee = new Employee("John", "IT", 50000);
        employee.setId(1);
        when(empService.getEmployeeById(1)).thenReturn(employee);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Employee.id").value(1))
                .andExpect(jsonPath("$.Employee.name").value("John"))
                .andExpect(jsonPath("$.Employee.department").value("IT"))
                .andExpect(jsonPath("$.Employee.salary").value(50000));
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeByIdDoesNotExist() throws Exception {
        when(empService.getEmployeeById(99))
                .thenThrow(new EmployeeNotFoundException("Employee with id 99not found"));

        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee with id 99not found"));
    }

}
