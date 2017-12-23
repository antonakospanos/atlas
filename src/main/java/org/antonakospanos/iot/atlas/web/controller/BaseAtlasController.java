package org.antonakospanos.iot.atlas.web.controller;

import org.antonakospanos.iot.atlas.web.dto.response.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(BaseAtlasController.class);

	protected final static String RESPONSE = "Not available yet!";

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseBase> handleMissingParams(MissingServletRequestParameterException exception) {
		ResponseBase validationError = new ResponseBase();

		String errorMsg = " The " + exception.getParameterName() + " parameter of the request is required!";
		validationError.setResult(Result.BAD_REQUEST);
		validationError.setDescription(errorMsg);
		logger.error(validationError.getResult() + ": " + validationError.getDescription(), exception);

		return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseBase> handleValidationException(IllegalArgumentException exception) {
		ResponseBase validationError = new ResponseBase();

		validationError.setResult(Result.BAD_REQUEST);
		validationError.setDescription(exception.getMessage());
		logger.error(exception.getClass() + " Cause: " + exception.getCause() + " Message: " + exception.getMessage());

		return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
	}
}
