package com.bridgelabz.employeepayrollapp;

import java.time.LocalDate;

public class EmployeePayrollData {
	public int id;
	public String name;
	public char gender;
	public double salary;
	public LocalDate startDate;
	
	public EmployeePayrollData(Integer id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.salary =salary;
	}
	public EmployeePayrollData(Integer id, String name, char gender,  double salary, LocalDate startDate) {
		this(id,name,salary);
		this.gender = gender;
		this.startDate = startDate;
	}
	@Override
	public String toString() {
		return "id: "+this.id+" name: "+this.name+" salary: "+this.salary;
	}
	
	@Override
	public boolean equals( Object obj) {
		if(obj == this) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		
		EmployeePayrollData that = (EmployeePayrollData) obj;
		return this.id == that.id && this.name.equals(that.name) && Double.compare(that.salary, this.salary) ==0;
	}
	
}