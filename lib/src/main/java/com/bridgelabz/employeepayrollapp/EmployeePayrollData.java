package com.bridgelabz.employeepayrollapp;

import java.time.LocalDate;

public class EmployeePayrollData {
	int employeeId;
	String employeeName;
	double employeeSalary;
	private LocalDate startDate;
	
	public EmployeePayrollData(int employeeId, String employeeName, Double employeeSalary) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
	}
	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
		this.employeeId = id;
		this.employeeName = name;
		this.employeeSalary = salary;
		this.startDate = startDate;

	}
	@Override
	public String toString() {
		return "EmployeePayrollData [employeeId=" + employeeId + ", employeeName=" + employeeName + ", employeeSalary="
				+ employeeSalary + "]";
	}
}
