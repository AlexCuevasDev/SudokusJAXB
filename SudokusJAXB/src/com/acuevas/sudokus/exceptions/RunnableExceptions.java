package com.acuevas.sudokus.exceptions;

@SuppressWarnings("serial")
public class RunnableExceptions extends Exception {
//TODO EXCEPTIONS THAT DOESN'T STOP THE PROGRAM

	public enum RunErrors {
		USER_NOT_FOUND_OR_INCORRECT_PASSWORD("");

		private String message;

		private RunErrors(String message) {
			this.message = message;
		}
	};

	public RunnableExceptions(RunErrors error) {

	}
}
