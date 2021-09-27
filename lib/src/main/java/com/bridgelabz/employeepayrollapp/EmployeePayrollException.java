package com.bridgelabz.employeepayrollapp;

public class EmployeePayrollException extends RuntimeException {
	enum ExceptionType {
        CONNECTION_FAIL, CANNOT_EXECUTE_QUERY, UPDATE_FAILED
    }

    ExceptionType exceptionType;

    public EmployeePayrollException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
