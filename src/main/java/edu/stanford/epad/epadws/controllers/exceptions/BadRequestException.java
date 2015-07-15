package edu.stanford.epad.epadws.controllers.exceptions;

import java.util.Date;

public class BadRequestException extends ControllerException {

	public BadRequestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message, Date date, String level) {
		super(message, date, level);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message, String level) {
		super(message, level);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
