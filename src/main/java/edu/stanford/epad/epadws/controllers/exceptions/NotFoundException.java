package edu.stanford.epad.epadws.controllers.exceptions;

import java.util.Date;

public class NotFoundException extends ControllerException {

	public NotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotFoundException(String message, Date date, String level) {
		super(message, date, level);
		// TODO Auto-generated constructor stub
	}

	public NotFoundException(String message, String level) {
		super(message, level);
		// TODO Auto-generated constructor stub
	}

	public NotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
