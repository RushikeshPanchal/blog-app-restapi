package com.blog.app.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resourceName;
	private String fieldNmae;
	private long fieldValue;
	
	
	public ResourceNotFoundException(String resourceName, String fieldNmae, long fieldValue) {
		super(String.format("%s not found with %s: '%s'", resourceName,fieldNmae, fieldValue)); 
		this.resourceName = resourceName;
		this.fieldNmae = fieldNmae;
		this.fieldValue = fieldValue;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getFieldNmae() {
		return fieldNmae;
	}

	public long getFieldValue() {
		return fieldValue;
	}	
	

}
