package com.bridgelabz.employeepayrollapp;

public class EmployeePayrollData {
	int employeeId;
	String employeeName;
	double employeeSalary;
	
	public EmployeePayrollData(int employeeId, String employeeName, double employeeSalary) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
	}
	@Override
	public String toString() {
		return "EmployeePayrollData [employeeId=" + employeeId + ", employeeName=" + employeeName + ", employeeSalary="
				+ employeeSalary + "]";
	}
}
