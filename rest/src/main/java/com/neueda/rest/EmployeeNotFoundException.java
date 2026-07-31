package com.neueda.rest;

public class EmployeeNotFoundException extends RuntimeException{

    public EmployeeNotFoundException(String message)
    {
        super(message);
    }

}
