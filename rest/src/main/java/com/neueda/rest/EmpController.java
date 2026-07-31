package com.neueda.rest;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")

public class EmpController {
    @Autowired
    private EmpService empService;

    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllEmployees()
    {
        List<Employee> employees=empService.getAllEmployees();
        Map<String,Object> response=new HashMap<>();
        response.put("Messagge : ","All Employees retrieved successfully");
        response.put("Employess: ",employees);
        return ResponseEntity.status(200).body(response);

    }



    @PostMapping
    public ResponseEntity<Map<String,Object>> addEmployeee(@RequestBody Employee emp)
    {
        empService.addEmployee(emp);
        Map<String,Object> response=new HashMap<>();
        response.put("Message:","Employee addded successfully");
        response.put("Employee:",emp);

        return ResponseEntity.status(201).body(response);

    }

    @DeleteMapping("/{id}")
public ResponseEntity<Map<String,Object>> deleteEmployee(@PathVariable int id)
    {
        Employee employee=empService.getEmployeeById(id);
        Map<String,Object> response=new HashMap<>();
        if(employee!=null)
        {
            empService.deleteEmployee(id);
            response.put("Message :","Employee deleted successfully");
            return ResponseEntity.ok(response);
        }
        else{
            response.put("Message:"," Employee not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String,Object>> updateEmployee(@PathVariable int id,@RequestBody Employee emp)
    {
        Employee existingEmployee=empService.getEmployeeById(id);
        Map<String,Object> response=new HashMap<>();
        if(existingEmployee!=null)
        {
            empService.updateEmployee(id,emp);
            response.put("Message: ","Employee updated successfully");
            response.put("Employee : ",emp);
            return ResponseEntity.ok(response);
        }
        else
        {
            response.put("message : ","Employee not found");
            return ResponseEntity.status(404).body(response);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> getEmployeeById(@PathVariable int id)
    {
        Employee emp= empService.getEmployeeById(id);
        Map<String,Object> response=new HashMap<>();
        if(emp!=null)
        {
            response.put("Message:","Employee retrieved successsfully");
            response.put("Employee",emp);
            return ResponseEntity.status(200).body(response);

        }
        else{
            response.put("Message : ","Employee Not Found");
            response.put("Employee" ,null);
            return ResponseEntity.status(404).body(response);
        }
    }
}
