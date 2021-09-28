package com.bridgelabz.employeepayrollapp;

public class EmployeePayrollException extends RuntimeException {
	enum ExceptionType {
        FAILED_TO_CONNECT, CANNOT_EXECUTE_QUERY, UPDATE_FAILED
    }

    ExceptionType exceptionType;

    public EmployeePayrollException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
