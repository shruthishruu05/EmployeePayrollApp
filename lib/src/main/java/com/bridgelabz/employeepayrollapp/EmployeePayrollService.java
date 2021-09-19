package com.bridgelabz.employeepayrollapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
	
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO;
		
	}
	private List<EmployeePayrollData> employeePayrollList;
	
	public EmployeePayrollService() {
	}
	
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList)
	{
		this.employeePayrollList = employeePayrollList;
	}
	
	private void readEmployeePayrollData(Scanner consoleInputReader)
	{
		System.out.println("Enter employee id");
		int id = consoleInputReader.nextInt();
		System.out.println("enter employee name");
		String empName =  consoleInputReader.next();
		System.out.println("Enter employee salary");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id, empName, salary));
	}
	public void writeEmployeePayrollData(IOService ioService) {
		if(ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll Roster to Console\n" + employeePayrollList);
		
		else if(ioService.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
	}
	
	public static void main(String [] args)
	{
		System.out.println("Welcome to employee payroll application");
		ArrayList<EmployeePayrollData> employeePayrollList  = new ArrayList<EmployeePayrollData>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInputReader = new Scanner(System.in);
		
		employeePayrollService.readEmployeePayrollData(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
		
	
	}
	
}
