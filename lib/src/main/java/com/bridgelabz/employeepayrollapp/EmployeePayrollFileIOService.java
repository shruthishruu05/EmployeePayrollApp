package com.bridgelabz.employeepayrollapp;


	import java.io.File;
	import java.io.IOException;
	import java.nio.file.Files;
	import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

	public class EmployeePayrollFileIOService {

		public static String PAYROLL_FILE_NAME = "payroll-file.txt";

		public void writeData(List<EmployeePayrollData> employeePayrollList) {
			
			StringBuffer empBuffer = new StringBuffer();
			employeePayrollList.forEach(employee -> {
				String employeeDataString = employee.toString().concat("\n");
				empBuffer.append(employeeDataString);
			});

			try {
				Files.write(Paths.get(PAYROLL_FILE_NAME), empBuffer.toString().getBytes());

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		public void printData() {
			
			try {
				Files.lines(new File(PAYROLL_FILE_NAME).toPath()).forEach(System.out::println);
			}
			catch(IOException e) {e.printStackTrace();}
			
		}

		public long countEntries() {
			
			long entries=0;
			try {
				entries = Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
			}
			catch(IOException e) {e.printStackTrace();};
			return entries;
		}
		public List<String> readDataFromFile() {
			
			List<String> employeePayrollList = new ArrayList<String>();
			
			try {
				Files.lines(new File(PAYROLL_FILE_NAME).toPath())
					.map(employee -> employee.trim())
					.forEach(employee -> {
						System.out.println(employee);
						employeePayrollList.add(employee);
				});
				
			}
			catch(IOException e){
				e.printStackTrace();
			}
			return employeePayrollList;
		}
	}
