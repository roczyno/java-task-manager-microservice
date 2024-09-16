package com.roczyno.taskservice.exception;

import jakarta.validation.ConstraintViolation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TaskException.class)
	public ResponseEntity<ErrorDetails> taskExceptionHandler(TaskException ue, WebRequest req) {
		ErrorDetails err = new ErrorDetails(ue.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDetails> handleDatabaseConstraintViolationException(ConstraintViolationException cve) {
		String errorMessage = "Database Constraint Violation: " + cve.getConstraintName();
		ErrorDetails err = new ErrorDetails(errorMessage, "Database Constraint Violation", LocalDateTime.now());

		return new ResponseEntity<>(err, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me) {
		String errorMessage = me.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage())
				.collect(Collectors.joining(", "));

		ErrorDetails err = new ErrorDetails(errorMessage, "Validation Error", LocalDateTime.now());

		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
	public ResponseEntity<ErrorDetails> handleValidationException(jakarta.validation.ConstraintViolationException cve) {
		String errorMessage = cve.getConstraintViolations()
				.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.joining(", "));

		ErrorDetails err = new ErrorDetails(errorMessage, "Validation Error", LocalDateTime.now());

		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> otherExceptionsHandler(Exception ex, WebRequest req) {
		ErrorDetails err = new ErrorDetails(ex.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
