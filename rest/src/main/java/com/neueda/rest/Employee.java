package com.neueda.rest;

public class Employee {
    int id;
    String name;
    String department;

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int salary;


    Employee()
    {

    }
    Employee(String name,String department,int salary)
    {
        this.name=name;
        this.department=department;
        this.salary=salary;
    }

}
