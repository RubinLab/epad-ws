package edu.stanford.epad.epadws.controllers.exceptions;

import java.util.Date;

public class InvalidParameterException extends ControllerException {

	public InvalidParameterException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidParameterException(String message, Date date, String level) {
		super(message, date, level);
		// TODO Auto-generated constructor stub
	}

	public InvalidParameterException(String message, String level) {
		super(message, level);
		// TODO Auto-generated constructor stub
	}

	public InvalidParameterException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
