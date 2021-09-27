package com.bridgelabz.employeepayrollapp;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mysql.jdbc.Connection;

public class EmployeePayrollService {
	
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO;
		
	}
	private List<EmployeePayrollData> employeePayrollList;
	
	private EmployeePayrollDBService employeePayrollDBService;
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList)
	{
		this.employeePayrollList = employeePayrollList;
	}
	
	public EmployeePayrollService() {
		employeePayrollDBService =  EmployeePayrollDBService.getInstance();
	}
	
	private EmployeePayrollData getEmployeePayrollData(String name) {
			
			return this.employeePayrollList.stream()
					.filter(EmployeePayrollDataItem -> EmployeePayrollDataItem.employeeName.equals(name))
					.findFirst()
					.orElse(null);
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
	
	public void printData(IOService fileIo) {
		if(fileIo.equals(IOService.FILE_IO)) new EmployeePayrollFileIOService().printData();
	}


	public long countEntries(IOService fileIo) {
		if(fileIo.equals(IOService.FILE_IO)) return new EmployeePayrollFileIOService().countEntries();
		
		return 0;
	}
	public long readDataFromFile(IOService fileIo) {
			
			List<String> employeePayrollFromFile = new ArrayList<String>();
			if(fileIo.equals(IOService.FILE_IO)) {
				System.out.println("Employee Details from payroll-file.txt");
				employeePayrollFromFile = new EmployeePayrollFileIOService().readDataFromFile();
				
			}
			return employeePayrollFromFile.size();
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

	public List<EmployeePayrollData> readEmployeePayrollData(IOService dbIo) {
		try {
			if(dbIo.equals(IOService.DB_IO))
				this.employeePayrollList = new EmployeePayrollDBService().readData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    return this.employeePayrollList;
			
	}

	public void updateEmployeeSalary(String name, double salary) {
		try {
			int result = new EmployeePayrollDBService().updateEmployeeSalary(name, salary);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void updateEmployeeSalaryUsingStatement(String name, double salary) throws SQLException {
			
			int result = employeePayrollDBService.updateEmployeeDataUsingStatement(name,salary);
			if(result == 0) 
				return;
			
			EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
			if(employeePayrollData != null)
				employeePayrollData.employeeSalary = salary;		
		}
	public boolean checkEmployeePayrollInSyncWithDB(String name) {
			
			List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
			return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
		}
	public List<EmployeePayrollData> getEmployeeDetailsBasedOnName(IOService ioService, String name) {
		if(ioService.equals(IOService.DB_IO))
			this.employeePayrollList = employeePayrollDBService.getEmployeeDetailsBasedOnNameUsingStatement(name);
		return this.employeePayrollList;
	}
	
}