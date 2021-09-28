package com.bridgelabz.employeepayrollapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.mysql.jdbc.Connection;
import java.sql.*;

public class EmployeePayrollDBService {

	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;
	private EmployeePayrollDBService() {}
	
	public static EmployeePayrollDBService getInstance() {
		if(employeePayrollDBService == null) {
			employeePayrollDBService = new EmployeePayrollDBService();
		}
		return employeePayrollDBService;
	}
	
	public List<EmployeePayrollData> readData() {
		String sql = "SELECT * FROM employee_payroll";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			employeePayrollList = this.getEmployeePayrollData(result);
			connection.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	
	
	public Connection getConnection() throws EmployeePayrollException
	{
		Connection connection;
		try {
			String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?userSSL=false";
			String userName = "root";
			String password = "Welcome@123";
			System.out.println("Connecting to database:"+jdbcURL);
			connection =  (Connection) DriverManager.getConnection(jdbcURL,userName,password);
			System.out.println("Connection is successful!!!!"+connection);
		}
		catch(SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.FAILED_TO_CONNECT, "Failed to connect to database");
		}
		return connection;
	
	}
	
	public int updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
		return this.updateEmployeeDataUsingStatement(name,salary);
	}
	
	private int updateEmployeeDataUsingStatement(String name,double salary) {
		String sqlString = String.format("update employee_payroll set salary = %2f where name = '%s';",salary,name);
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sqlString);
		
		}
		catch(SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UPDATE_FAILED, "Failed to update the given data");
		}
	
	}
	
	
	
	public void writeDB(List<EmployeePayrollData> employees) {
		employees.stream().forEach(employee ->{
			String sql = String.format("INSERT INTO employee_payroll(name,gender,salary,start)VALUES('%s','%s','%2f','%s')",employee.name,employee.gender,
										employee.salary,employee.startDate.toString());
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				int result = statement.executeUpdate(sql);
				connection.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		});
		
	}
	
	public int countEntries() {
		String sql = "SELECT * FROM employee_payroll";
		int count  =0;
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				count++;
			}
			connection.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	
	
	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		
		try {
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				char gender = resultSet.getString("gender").charAt(0);
				double basicSalary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, gender,basicSalary, startDate));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		List<EmployeePayrollData> employeePayrollDataList = null;
		if(this.employeePayrollDataStatement == null) {
			this.prepareStatementForEmployeeData();
		}
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollDataList = this.getEmployeePayrollData(resultSet);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollDataList;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sqlStatement = "select * from employee_payroll where name = ?;";
			employeePayrollDataStatement = connection.prepareStatement(sqlStatement);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<EmployeePayrollData> getEmployeesInDateRange(String date1, String date2) {
		List<EmployeePayrollData> employeePayrollList = null;
		String sql = String.format("select * from employee_payroll where start between cast('%s' as date) and cast('%s' as date)",date1,date2);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			employeePayrollList = this.getEmployeePayrollData(result);
			connection.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return employeePayrollList;
	}
	
	public HashMap<Character, Double> getGenderWiseTotalSalary() throws EmployeePayrollException{
		HashMap<Character,Double> salaryMap = new HashMap<>();
		String sql = "SELECT gender , SUM(salary) as 'SUM'  FROM employee_payroll GROUP BY gender;";
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				char key = result.getString("gender").charAt(0);
				double value = result.getDouble("SUM");
				salaryMap.put(key, value);
			}
		}
		catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "Cannot execute the query");
		}	
		return salaryMap;
	}
	
	public HashMap<Character, Double> getGenderWiseMinSalary() throws EmployeePayrollException{
		HashMap<Character,Double> salaryMap = new HashMap<>();
		String sql = "SELECT gender , MIN(salary) as 'MIN'  FROM employee_payroll GROUP BY gender;";
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				char key = result.getString("gender").charAt(0);
				double value = result.getDouble("MIN");
				salaryMap.put(key, value);
			}
		}
		catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "Cannot execute the query");
		}	
		return salaryMap;
	}
	
	public HashMap<Character, Double> getGenderWiseMaxSalary() throws EmployeePayrollException{
		HashMap<Character,Double> salaryMap = new HashMap<>();
		String sql = "SELECT gender , MAX(salary) as 'MAX'  FROM employee_payroll GROUP BY gender;";
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				char key = result.getString("gender").charAt(0);
				double value = result.getDouble("MAX");
				salaryMap.put(key, value);
			}
		}
		catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "Cannot execute the query");
		}	
		return salaryMap;
	}
	
	public HashMap<Character, Double> getGenderWiseAvgSalary() throws EmployeePayrollException{
		HashMap<Character,Double> salaryMap = new HashMap<>();
		String sql = "SELECT gender , AVG(salary) as 'AVG'  FROM employee_payroll GROUP BY gender;";
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				char key = result.getString("gender").charAt(0);
				double value = result.getDouble("Avg");
				salaryMap.put(key, value);
			}
		}
		catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "Cannot execute the query");
		}	
		return salaryMap;
	}
	
	public HashMap<Character, Integer> getGenderWiseCount() throws EmployeePayrollException{
		HashMap<Character,Integer> countMap = new HashMap<>();
		String sql = "SELECT gender , COUNT(salary) as 'COUNT'  FROM employee_payroll GROUP BY gender;";
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				char key = result.getString("gender").charAt(0);
				int value = result.getInt("COUNT");
				countMap.put(key, value);
			}
		}
		catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "Cannot execute the query");
		}	
		return countMap;
	}
	public EmployeePayrollData addEmployeeToPayroll7(String name, Double salary, LocalDate startDate, char gender) {
		int employeeID = -1;
		EmployeePayrollData employeePayrollData = null;
		String sql = String.format("INSERT INTO employee_payroll(name,gender,salary,start)VALUES('%s','%s','%2f','%s')",name,gender,
				salary,startDate.toString());
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			int result = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			if(result == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) employeeID = resultSet.getInt(1);
			}
			connection.close();
			employeePayrollData = new EmployeePayrollData(employeeID, name, gender,salary, startDate);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println(employeePayrollData);
		return employeePayrollData;
	}

	public EmployeePayrollData addEmployeeToPayroll(String name, Double salary, LocalDate startDate, char gender) {
		int employeeID = -1;
		EmployeePayrollData employeePayrollData = null;
		Connection connection = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		}
		catch(Exception e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.FAILED_TO_CONNECT, "couldn't establish connection");
		}
		
		try (Statement statement = connection.createStatement();){
			String sql = String.format("INSERT INTO employee_payroll(name,gender,salary,start)VALUES('%s','%s','%2f','%s')",name,gender,
					salary,startDate.toString());
			int result = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			if(result == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) employeeID = resultSet.getInt(1);
			}
		}
		catch(SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "query execution failed");
		}
		
		try(Statement statement = connection.createStatement();){
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format("INSERT INTO payroll_details(employee_id, basicPay, deductions, taxablePay, incomeTax, netPay)VALUES(%d,%2f,%2f,%2f,%2f,%2f)",
					employeeID,salary,deductions,taxablePay,tax,netPay);
			int result = statement.executeUpdate(sql);
			if(result == 1) {
				employeePayrollData = new EmployeePayrollData(employeeID, name, gender,salary, startDate);
			}
		}
		catch(SQLException e) {
			try {
				connection.rollback();
			} 
			catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "query execution failed");
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return employeePayrollData;
	}
		
}

