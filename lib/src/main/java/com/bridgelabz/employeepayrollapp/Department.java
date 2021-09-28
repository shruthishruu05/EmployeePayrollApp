package com.bridgelabz.employeepayrollapp;

public class Department {
	private String departmentName;
	private String departmentId;
	private String hod;
	
	public String getDepartmentName() {
		return departmentName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public String getHod() {
		return hod;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public void setHod(String hod) {
		this.hod = hod;
	}

	public Department(String departmentName, String departmentId, String hod) {
		super();
		this.departmentName = departmentName;
		this.departmentId = departmentId;
		this.hod = hod;
	}
}
