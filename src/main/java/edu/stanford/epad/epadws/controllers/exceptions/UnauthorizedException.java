package edu.stanford.epad.epadws.controllers.exceptions;

import java.util.Date;

public class UnauthorizedException extends ControllerException {

	public UnauthorizedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnauthorizedException(String message, Date date, String level) {
		super(message, date, level);
		// TODO Auto-generated constructor stub
	}

	public UnauthorizedException(String message, String level) {
		super(message, level);
		// TODO Auto-generated constructor stub
	}

	public UnauthorizedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
