package com.bridgelabz.employeepayrollapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabz.employeepayrollapp.EmployeePayrollException.ExceptionType;
import com.mysql.jdbc.Connection;
import java.sql.*;

public class EmployeePayrollDBService {
	
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;
	private static List<EmployeePayrollData> employeePayrollList;
	public static EmployeePayrollDBService getInstance() {
		if(employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}
	
	public void displayDate() {
		System.out.println(employeePayrollList);
	}
	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
		
		employeePayrollList = new ArrayList<>();
		
		try {
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double basicSalary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, basicSalary, startDate));
				
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
		
	}
	
	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		
		List<EmployeePayrollData> employeePayrollList = null;
		if(this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1,name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	
	public List<EmployeePayrollData> readData() {
		String sql = "SELECT * FROM employee_payroll"; 
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int id = result.getInt("id");
				String name = result.getString("id");
				double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary,startDate));
				
			}
			connection.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	public Connection getConnection() throws SQLException
	{
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?userSSL=false";
		String userName = "root";
		String password = "Welcome@123";
		Connection connection;
		System.out.println("Connecting to database:"+jdbcURL);
		connection =  (Connection) DriverManager.getConnection(jdbcURL,userName,password);
		System.out.println("Connection is successful!!!!"+connection);
		return connection;
	
	}
	
	public int updateEmployeeSalary(String name, double salary) throws SQLException {
		return this.updateEmployeeDataUsingStatement(name,salary);
		
	}
	
	int updateEmployeeDataUsingStatement(String name,double salary) throws SQLException {
		String sqlString = String.format("update employee_payroll set salary = %2f where name = '%s';",salary,name);
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sqlString);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	private void prepareStatementForEmployeeData() {
			
			try {
				Connection connection = this.getConnection();
				String sqlStatement = "SELECT * FROM employee_payroll WHERE name = ?;";
				employeePayrollDataStatement = connection.prepareStatement(sqlStatement);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}

	public List<EmployeePayrollData> getEmployeeDetailsBasedOnNameUsingStatement(String name) {
		String sqlStatement = String.format("SELECT * FROM employee_payroll WHERE name = '%s';",name);
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
				
		try (Connection connection = getConnection();){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	
	public List<EmployeePayrollData> getEmployeeDetailsBasedOnStartDateUsingStatement(String startDate) {
			
			String sqlStatement = String.format("SELECT * FROM employee_payroll WHERE start BETWEEN CAST('%s' AS DATE) AND DATE(NOW());",startDate);
			List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
					
			try (Connection connection = getConnection();){
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);
				employeePayrollList = this.getEmployeePayrollData(resultSet);
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return employeePayrollList;
		}

	public List<EmployeePayrollData> getEmployeeDetailsBasedOnStartDateUsingPreparedStatement(String startDate) {
		List<EmployeePayrollData> employeePayrollList = null;
		if(this.employeePayrollDataStatement == null)
			this.preparedStatementForEmployeeDataBasedOnStartDate();
		try {
			employeePayrollDataStatement.setString(1,startDate);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

		private void preparedStatementForEmployeeDataBasedOnStartDate() {
			
			try {
				Connection connection = this.getConnection();
				String sqlStatement = "SELECT * FROM employee_payroll WHERE start BETWEEN CAST(? AS DATE) AND DATE(NOW());";
				employeePayrollDataStatement = connection.prepareStatement(sqlStatement);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		public List<Double> getSumOfSalaryBasedOnGenderUsingStatement() {
			
			String sqlStatement = "SELECT gender, SUM(salary) AS TotalSalary FROM employee_payroll GROUP BY gender;";
			List<Double> sumOfSalaryBasedOnGender = new ArrayList<>();
					
			try (Connection connection = getConnection();){
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);
				while(resultSet.next()) {
					double salary = resultSet.getDouble("TotalSalary");
					sumOfSalaryBasedOnGender.add(salary);
				}
			}
			catch(SQLException e){
				throw new EmployeePayrollException(ExceptionType.CANNOT_EXECUTE_QUERY, "Could not Execute Query! Check the Syntax");
			}
			return sumOfSalaryBasedOnGender;
		}
		
		public List<Double> getAverageOfSalaryBasedOnGenderUsingStatement() {
			
			String sqlStatement = "SELECT gender, AVG(salary) AS AverageSalary FROM employee_payroll GROUP BY gender;";
			List<Double> averageOfSalaryBasedOnGender = new ArrayList<>();
					
			try (Connection connection = getConnection();){
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);
				while(resultSet.next()) {
					double salary = resultSet.getDouble("AverageSalary");
					averageOfSalaryBasedOnGender.add(salary);
				}
			}
			catch(SQLException e){
				throw new EmployeePayrollException(ExceptionType.CANNOT_EXECUTE_QUERY, "Could not Execute Query! Check the Syntax");
			}
			return averageOfSalaryBasedOnGender;
		}
		
		public List<Double> getMinimumSalaryBasedOnGenderUsingStatement() {
			
			String sqlStatement = "SELECT gender, MIN(salary) AS MinimumSalary FROM employee_payroll GROUP BY gender;";
			List<Double> MinimumSalaryBasedOnGender = new ArrayList<>();
					
			try (Connection connection = getConnection();){
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);
				while(resultSet.next()) {
					double salary = resultSet.getDouble("MinimumSalary");
					MinimumSalaryBasedOnGender.add(salary);
				}
			}
			catch(SQLException e){
				throw new EmployeePayrollException(ExceptionType.CANNOT_EXECUTE_QUERY, "Could not Execute Query! Check the Syntax");

			}
			return MinimumSalaryBasedOnGender;
		}
		
		public List<Double> getMaximumSalaryBasedOnGenderUsingStatement() {
			
			String sqlStatement = "SELECT gender, MAX(salary) AS MaximumSalary FROM employee_payroll GROUP BY gender;";
			List<Double> MaximumSalaryBasedOnGender = new ArrayList<>();
					
			try (Connection connection = getConnection();){
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);
				while(resultSet.next()) {
					double salary = resultSet.getDouble("MaximumSalary");
					MaximumSalaryBasedOnGender.add(salary);
				}
			}
			catch(SQLException e){
				throw new EmployeePayrollException(ExceptionType.CANNOT_EXECUTE_QUERY, "Could not Execute Query! Check the Syntax");

			}
			return MaximumSalaryBasedOnGender;
		}

		public List<Integer> getCountOfEmployeesBasedOnGenderUsingStatement() {
			
			String sqlStatement = "SELECT gender, COUNT(gender) AS CountBasedOnGender FROM employee_payroll GROUP BY gender;";
			List<Integer> CountBasedOnGender = new ArrayList<>();
					
			try (Connection connection = getConnection();){
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);
				while(resultSet.next()) {
					int count = resultSet.getInt("CountBasedOnGender");
					CountBasedOnGender.add(count);
				}
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return CountBasedOnGender;
		}
		
		
	
	}
	

