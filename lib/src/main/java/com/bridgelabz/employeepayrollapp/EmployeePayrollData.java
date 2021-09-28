package com.bridgelabz.employeepayrollapp;

import java.time.LocalDate;
import java.util.List;

public class EmployeePayrollData {
	public int id;
	public String name;
	public char gender;
	public double salary;
	public LocalDate startDate;
	public String phoneNumber;
	public String address;
	public Company company;
	public List<Department> departments;
	
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
	public EmployeePayrollData(Integer id, String name, char gender,  double salary, String address, String phoneNumber, LocalDate startDate, Company company, List<Department> departments) {
		this(id,name,gender,salary,startDate);
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.company = company;
		this.departments = departments;
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