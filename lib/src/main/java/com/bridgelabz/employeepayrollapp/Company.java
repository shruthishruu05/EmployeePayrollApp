package com.bridgelabz.employeepayrollapp;

public class Company {
	private String companyName;
	private int companyId;
	

	public String getCompanyName() {
		return companyName;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public Company(String companyName, int companyId) {
		super();
		this.companyName = companyName;
		this.companyId = companyId;
	}
	@Override
	public String toString() {
		return "Company [companyName=" + companyName + ", companyId=" + companyId + "]";
	}
}
