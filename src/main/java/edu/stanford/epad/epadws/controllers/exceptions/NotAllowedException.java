package edu.stanford.epad.epadws.controllers.exceptions;

import java.util.Date;

public class NotAllowedException extends ControllerException {

	public NotAllowedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotAllowedException(String message, Date date, String level) {
		super(message, date, level);
		// TODO Auto-generated constructor stub
	}

	public NotAllowedException(String message, String level) {
		super(message, level);
		// TODO Auto-generated constructor stub
	}

	public NotAllowedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
