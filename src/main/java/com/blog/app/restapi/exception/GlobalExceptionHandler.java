package com.blog.app.restapi.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.blog.app.restapi.dto.ErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	// handle specific exception (ResurceNotFoundException)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundExceptions(ResourceNotFoundException exception, 
			                                                             WebRequest webRequest){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	// handle specific exception (BlogAPIException)
		@ExceptionHandler(BlogAPIException.class)
		public ResponseEntity<ErrorDetails> handleBlogAPIException(BlogAPIException exception, 
				                                                             WebRequest webRequest){
			ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),webRequest.getDescription(false));
			return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.BAD_REQUEST);
		}
		
	// global exceptions, handles all exceptions type except above two 
		@ExceptionHandler(Exception.class)
		public ResponseEntity<ErrorDetails> hadleGlobalException(Exception exception, 
				                                                             WebRequest webRequest){
			ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),webRequest.getDescription(false));
			return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	// handling postDto fieldvalidations custom exception with proper error response
		@Override
		protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
				                                                      HttpHeaders headers, 
				                                                      HttpStatus status, 
				                                                      WebRequest request){
       Map<String, String> errors = new HashMap<>();
       ex.getBindingResult().getAllErrors().forEach((error) ->{
    	   String fieldName = ((FieldError)error).getField();
    	   String message = error.getDefaultMessage();
    	   errors.put(fieldName, message);
       });
	   return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
      }
		
  // second approach by specific exceptions for all the field in postDto class
	/*	@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, 
				                                                             WebRequest webRequest){
		    Map<String, String> errors = new HashMap<>();
		    exception.getBindingResult().getAllErrors().forEach((error) ->{
		    	   String fieldName = ((FieldError)error).getField();
		    	   String message = error.getDefaultMessage();
		    	   errors.put(fieldName, message);
		       });
		       return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}*/
}
