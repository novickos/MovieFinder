package com.example.aoim_plz_dzialaj;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassEmployee {
    private String groupName;
    private int maxNumber=10;
    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    private final IntegerProperty currentSize = new SimpleIntegerProperty(0);

    public String getName(){
        return groupName;
    }

    public int getSize(){
        return employees.size();
    }

    public int getMaxEmployees(){return maxNumber;}

    public ClassEmployee() {
        this.groupName="";
        this.maxNumber=0;
    }

    public ClassEmployee(String groupName,int maxNumber) {
        this.groupName=groupName;
        this.maxNumber=maxNumber;

    }


    public ClassEmployee(String groupName, ObservableList<Employee> employees, int maxNumber) {
        this.groupName = groupName;
        this.employees = employees;
        this.maxNumber = maxNumber;

        employees.addListener((ListChangeListener<Employee>) change -> {
            currentSize.set(employees.size());
        });

    }

    public IntegerProperty currentSizeProperty() {
        return currentSize;
    }

    public int getCurrentSize() {
        return currentSize.get();
    }

    public void addEmployee(Employee e) {   //ZOBACZ CZY DZIALA
        if ( employees.size() >= maxNumber) {
            System.out.println("Max number of employees already reached");
        }

        if (employees.contains(e)) {
            System.out.println("Employee already exists");
        }
        else {
            employees.add(e);
            currentSize.set(employees.size());

        }
    }

    public ObservableList<Employee> getEmployees() {
        return employees;
    }

    public void removeEmployee(Employee e) {
        employees.remove(e);
        currentSize.set(employees.size());

    }

    public void addSalary(Employee e, double s) {
        e.setSalary(e.getSalary() + s);
    }

    public void changeCondition(Employee e, EmployeeCondition c) {
        e.setCondition(c);
    }

    public List<Employee> search(String surname){
        List<Employee> emp = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getLastName().compareTo(surname) == 0) {
                System.out.println("employee found");
                emp.add(e);

            }
            return emp;
        }
        System.out.println("employee not found");
        return null;

    }

    public List<Employee> searchPartial(String surname) {
        List<Employee> emp = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getLastName().contains(surname)) {
                System.out.println("employee found");
                emp.add(e);
            }
        }

        if (emp.isEmpty()){
            System.out.println("employee not found");
        }
        return emp;

    }

    public int countByCondition(EmployeeCondition c) {
        int count = 0;

        for (Employee e : employees) {
            if (e.getCondition() == c)
                count++;
        }
        System.out.println("Number of condition " + c + ": " + count);
        return count;
    }

    public void summary(){
        for (Employee e : employees) {
            e.print();
        }
    }


    public List<Employee> sortByName(){
    Collections.sort(employees); //hmmm
    return employees;
    }

    public List<Employee> sortBySalary() {
    Collections.sort(employees, new EmployeeComparator());
    return employees;
    }

    public Employee max(){
        return Collections.max(employees, new EmployeeComparator());
    }

    public boolean isEmpty(){
        return this.employees.isEmpty();
    }

    public String getGroupName(){return groupName;}


}

