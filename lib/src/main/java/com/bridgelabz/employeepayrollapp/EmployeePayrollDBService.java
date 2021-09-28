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

	public void addEmployeeToPayroll(String name,double salary, LocalDate startDate,  char gender) throws SQLException {
        int employeeID = -1;
        Connection connection = null;
        connection = this.getConnection();
        Statement statement = connection.createStatement();
        try {
            String sql = String.format("insert into employee_payroll(name,gender,salary,start) values" +"('%s','%s',%2f,CAST('%s' AS DATE))", name, startDate, salary, gender);
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {

                System.out.println(employeeID);
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employeeID = resultSet.getInt(1);
                System.out.println(employeeID);
          }
            countEntries();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format("INSERT INTO payroll_details" +
                   "(employee_id,basic_pay,deductions,taxable_pay,tax,net_pay) values"+"(%s,%s,%s,%s,%s,%s)",employeeID, salary,deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
	private HashMap<Integer,Company> getCompany(){
		HashMap<Integer, Company> companyMap = new HashMap<>();
		String sql = "select * from company";
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int id  = result.getInt("company_id");
				String name  = result.getString("company_name");
				companyMap.put(id, new Company(name, id));
			}
		}
		catch(SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "query execution failed");
		}
		return companyMap;
	}
	
	private HashMap<Integer,ArrayList<Department>> getDepartmentList(){
		HashMap<Integer,ArrayList<Department>> departmentList = new HashMap<>();
		String sql = "select * from employee_department";
		HashMap<String,Department> deptMap = getDepartment();
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int empId = result.getInt("employee_id");
				String deptId = result.getString("department_id");
				if(departmentList.get(empId) == null) departmentList.put(empId, new ArrayList<Department>());
				departmentList.get(empId).add(deptMap.get(deptId));
			}
		}
		catch(SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "query execution failed");
		}
		return departmentList;
	}
	
	private HashMap<String,Department> getDepartment(){
		String sql = "select * from department";
		HashMap<String,Department> set = new HashMap<>();
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				String id = result.getString("department_id");
				String name  = result.getString("department_name");
				String hod  = result.getString("hod");
				set.put(id,new Department(name, id, hod));
			}
		}
		catch(SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "query execution failed");
		}
		return set;
	}
	
	public int insertDepartment(Department dept) {
		int result = 0;
			String sql = String.format("INSERT INTO department(department_name,department_id,hod)VALUES('%s','%s','%s')",dept.getDepartmentId(),
					dept.getDepartmentName(),dept.getHod());
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				result = statement.executeUpdate(sql);
				connection.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		return result;
	}
	public int insertCompany(Company company) {
		int result = 0;
			String sql = String.format("INSERT INTO company(company_name,company_id)VALUES('%s',%d)",company.getCompanyName(),company.getCompanyId());
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				result = statement.executeUpdate(sql);
				connection.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		return result;
	}
	public int deleteEmployee(String name, List<EmployeePayrollData> employeeList) {
		int result = 0;
		String sql = String.format("update employee set is_active = false where name = '%s'",name);
		try(Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(sql);
			deleteEmployeeObject(name, employeeList);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.CANNOT_EXECUTE_QUERY, "Cannot execute the query");
		}
		return result;
	}
	
	private void deleteEmployeeObject(String name,List<EmployeePayrollData> employeeList) {
		for(EmployeePayrollData employee : employeeList) {
			if(employee.name.equals(name)) employeeList.remove(employee);
		}
	}
}

