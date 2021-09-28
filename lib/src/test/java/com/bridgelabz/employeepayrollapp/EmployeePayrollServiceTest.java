package com.bridgelabz.employeepayrollapp;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.bridgelabz.employeepayrollapp.EmployeePayrollService.IOService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class EmployeePayrollServiceTest {
    int size = 3;
	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = {
				new EmployeePayrollData(1, "Jeff Bezos", 10000),
				new EmployeePayrollData(2, "Bill Gates", 20000),
				new EmployeePayrollData(3, "Mark Zuckerberg", 30000)
		};
		
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		Assert.assertEquals(3,entries);
	}
	
	@Test
	public void givenFileOnReadingFromMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		long entries = employeePayrollService.readEmployeePayrollData(IOService.FILE_IO);
		Assert.assertEquals(3,entries);
	}
	
	@Test
	public void givenEmployeePayrollInDB_WhenRetrived_ShouldReturnEmployeeCount()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataDB(IOService.DB_IO);
		Assert.assertEquals(size, employeePayrollData.size());
	}

	
	@Test
	public void givenListOfEmployees_WhenInserted_ShouldMatchEmployeeEntries() throws SQLException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String date = "16-08-2019";
		LocalDate startDate = LocalDate.parse(date, formatter);
		size += 1;
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(new ArrayList<>()); 
		employeePayrollService.addEmployeeToPayroll("sita",100000.00,startDate,'F' );
		boolean result = employeePayrollService.checkEmployeePayrollInsyncWithDB("sita");
		Assert.assertTrue(result);
	}

	
	@Test
	public void givenNewSalaryForEmpoyee_WhenUpdated_ShouldSyncWithDB()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataDB(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Tanisha",500000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInsyncWithDB("Tanisha");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenDateRange_WhenQueried_ShouldReturnEmployeeList()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> empList = employeePayrollService.getEmpInADateRange("2018-01-03","2020-07-07");
		System.out.println(empList);
		Assert.assertEquals(5, empList.size());
	}
	
	@Test
	public void givenEmployeePayrollInDB_ShouldReturnSumOfSalaryBasedOnGender() {
		
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> salaryMap = employeePayrollService.getGenderWiseTotalSalary(IOService.DB_IO);
		Assert.assertEquals((double)salaryMap.get('F'),500000,0.0);
		Assert.assertEquals((double)salaryMap.get('M'),400000,0.0);
	}
	
	@Test
	public void givenEmployeePayrollInDB_ShouldReturnMinSalaryBasedOnGender() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> salaryMap = employeePayrollService.getGenderWiseMinSalary(IOService.DB_IO);
		Assert.assertEquals((double)salaryMap.get('F'),500000,0.0);
		Assert.assertEquals((double)salaryMap.get('M'),100000,0.0);
		
	}
	
	@Test
	public void givenEmployeePayrollInDB_ShouldReturnAverageSalaryBasedOnGender() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> salaryMap = employeePayrollService.getGenderWiseAvgSalary(IOService.DB_IO);
		Assert.assertEquals((double)salaryMap.get('F'),500000,0.0);
		Assert.assertEquals((double)salaryMap.get('M'),200000,0.0);
		
	}
	
	@Test
	public void givenEmployeePayrollInDB_ShouldReturnMaximumSalaryBasedOnGender() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Double> salaryMap = employeePayrollService.getGenderWiseMaxSalary(IOService.DB_IO);
		Assert.assertEquals((double)salaryMap.get('F'),500000,0.0);
		Assert.assertEquals((double)salaryMap.get('M'),300000,0.0);
		
	}
	
	@Test
	public void givenEmployeePayrollInDB_ShouldReturnCountOfBasedOnGender() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<Character, Integer> countMap = employeePayrollService.getGenderWiseCount(IOService.DB_IO);
		Assert.assertEquals((int)countMap.get('F'),6);
		Assert.assertEquals((int)countMap.get('M'),2);
		
	}
	
}