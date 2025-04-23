package com.example.aoim_plz_dzialaj;

import java.util.Comparator;

public class EmployeeComparator implements Comparator<Employee> {


    @Override
    public int compare(Employee o1, Employee o2) {
        return Double.compare( o1.getSalary(), o2.getSalary());
    }
}
