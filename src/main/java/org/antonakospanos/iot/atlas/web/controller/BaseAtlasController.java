package org.antonakospanos.iot.atlas.web.controller;

import org.antonakospanos.iot.atlas.web.dto.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class BaseAtlasController {

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseBase> handleMissingParams(MissingServletRequestParameterException exception) {
		ResponseBase validationError = new ResponseBase();

		String errorMsg = " The " + exception.getParameterName() + " parameter of the request is required!";
		validationError.setResult(Result.BAD_REQUEST);
		validationError.setDescription(errorMsg);

		return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseBase> handleValidationException(IllegalArgumentException exception) {
		ResponseBase validationError = new ResponseBase();

		validationError.setResult(Result.BAD_REQUEST);
		validationError.setDescription(exception.getMessage());

		return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
	}
}
