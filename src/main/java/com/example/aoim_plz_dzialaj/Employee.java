package com.example.aoim_plz_dzialaj;

public class Employee implements Comparable<Employee> {
    private String name;
    private String surname;
    private EmployeeCondition condition;
    private int birthYear;
    private double salary;

    public Employee(String name, String surname, EmployeeCondition condition, int birthYear, double salary) {
        this.name = name;
        this.surname = surname;
        this.condition = condition;
        this.birthYear = birthYear;
        this.salary = salary;
    }

    void print() {
        System.out.println(name + " " + surname + " " + condition + " " + birthYear + " " + salary);

    }

    public String getFirstName() {
        return name;
    }

    public String getLastName() {
        return surname;
    }

    public double getSalary() {
        return salary;
    }

    public void setCondition(EmployeeCondition condition) {
        this.condition = condition;
    }

    public EmployeeCondition getCondition() {
        return condition;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getBirthYear() {return birthYear;}


    @Override
    public int compareTo(Employee o) {
        int result = this.name.compareTo(o.getFirstName());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return name.equals(employee.name) && surname.equals(employee.surname);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthYear(int byear) {
        this.birthYear = byear;
    }
}
