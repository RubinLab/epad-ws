package edu.stanford.epad.epadws.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.epadws.controllers.exceptions.ForbiddenException;
import edu.stanford.epad.epadws.controllers.exceptions.InvalidParameterException;
import edu.stanford.epad.epadws.controllers.exceptions.NotAllowedException;
import edu.stanford.epad.epadws.controllers.exceptions.NotFoundException;
import edu.stanford.epad.epadws.controllers.exceptions.UnauthorizedException;

@ControllerAdvice
public class ControllerExceptionHandler {
	
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    EPADMessage handleException(InvalidParameterException ex) {
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    EPADMessage handleException(NotFoundException ex) {
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    EPADMessage handleException(NotAllowedException ex) {
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    EPADMessage handleException(ForbiddenException ex) {
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    EPADMessage handleException(UnauthorizedException ex) {
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    EPADMessage handleException(Exception ex) {
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }   
}
