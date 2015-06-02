package edu.stanford.epad.epadws.controllers.exceptions;

import java.util.Date;

public class ControllerException extends Exception {
	protected Date date;
	protected String level;
	public ControllerException(String message, Date date, String level) {
		super(message);
		this.date = date;
		this.level = level;
	}
	public ControllerException(String message, String level) {
		super(message);
		this.level = level;
		date = new Date();
	}
	public ControllerException(String message) {
		super(message);
		this.level = "ERROR";
		date = new Date();
	}
	public ControllerException() {
		super();
		this.level = "ERROR";
		date = new Date();
	}

}
