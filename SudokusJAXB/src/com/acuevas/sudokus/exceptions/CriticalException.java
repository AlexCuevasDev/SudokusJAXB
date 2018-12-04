package com.acuevas.sudokus.exceptions;

@SuppressWarnings("serial")
public class CriticalException extends Exception {

	private StructErrors error;

	public enum StructErrors {

		// @formatter:off
		PERSISTANCE_STRUCTURE("The structure of the .txt file is not supported."),
		SUDOKUES_NOT_CORRECT("The sudokus inserted are not correct."),
		MARSHALLER_ERROR("Error getting the Marshaller."),
		UNMARSHALLER_ERROR("Error getting the UnMarshaller."),
		GETTING_CONTEXT_ERROR("Error getting the context."),
		FILE_NOT_FOUND("Error, the program couldn't find the file."),
		CRITICAL_FAILURE("A critical error happened, the program will restart now.");
		// @formatter:on

		private String message;

		private StructErrors(String message) {
			this.message = message;
		}
	};

	public CriticalException(StructErrors error) {
		this.error = error;
		printStackTrace();
	}

	@Override
	public String getMessage() {
		return error.message;
	}

}