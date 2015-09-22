//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.epadws.controllers.exceptions.ForbiddenException;
import edu.stanford.epad.epadws.controllers.exceptions.InvalidParameterException;
import edu.stanford.epad.epadws.controllers.exceptions.NotAllowedException;
import edu.stanford.epad.epadws.controllers.exceptions.NotFoundException;
import edu.stanford.epad.epadws.controllers.exceptions.UnauthorizedException;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	private static final EPADLogger log = EPADLogger.getInstance();
	
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    EPADMessage handleException(InvalidParameterException ex) {
    	log.warning(ex.getMessage(), ex);
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }
    
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    EPADMessage handleException(NotFoundException ex) {
    	log.warning(ex.getMessage(), ex);
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }
    
    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    EPADMessage handleException(NotAllowedException ex) {
    	log.warning(ex.getMessage(), ex);
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }
    
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    EPADMessage handleException(ForbiddenException ex) {
    	log.warning(ex.getMessage(), ex);
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    EPADMessage handleException(UnauthorizedException ex) {
    	log.warning(ex.getMessage(), ex);
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    EPADMessage handleException(NullPointerException ex) {
    	log.warning(ex.getMessage(), ex);
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    EPADMessage handleException(Exception ex) {
    	log.warning(ex.getMessage(), ex);
    	EPADMessage errorMessage = new EPADMessage(ex.getMessage());
        return errorMessage;
    }   
}
