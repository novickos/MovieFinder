package com.example.aoim_plz_dzialaj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassContainer {
    Map<String, ClassEmployee> grupyPracownicze = new HashMap();


    void addNewClass(String name, int maxNumber) {
        ClassEmployee newEmployeeClass = new ClassEmployee(name, maxNumber);
        grupyPracownicze.put(name, newEmployeeClass);
    }

    void removeClass(String name) {
        grupyPracownicze.remove(name);
    }

    void addExistingClass(ClassEmployee employeeClass) {
        grupyPracownicze.put(employeeClass.getName(), employeeClass);

    }

    public List<ClassEmployee> findEmpty(){
        List<ClassEmployee> empty = new ArrayList<>();
        ClassEmployee newEmployee = new ClassEmployee();
        for (Map.Entry<String, ClassEmployee> entry : grupyPracownicze.entrySet()) {
            newEmployee = (ClassEmployee) entry.getValue();
            if (newEmployee.isEmpty())
                empty.add(newEmployee);
        }
        System.out.println("Number of empty groups: " + empty.size());
        return empty;
    }

    public void summary(){
        ClassEmployee newClassEmployee = new ClassEmployee();
        for(Map.Entry<String, ClassEmployee> entry : grupyPracownicze.entrySet()){
            newClassEmployee = (ClassEmployee) entry.getValue();
            if (newClassEmployee.isEmpty())
                System.out.println("0% full");
            else
                System.out.println(newClassEmployee.getName() + "full in: " + ((double)newClassEmployee.getSize()/(double)newClassEmployee.getMaxEmployees()*100) + "%");
        }
    }



}
