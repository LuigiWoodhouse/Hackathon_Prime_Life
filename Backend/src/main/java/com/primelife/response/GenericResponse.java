package com.primelife.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class GenericResponse implements Serializable {
	
	private static final long serialVersionUID = 1L; 
	private int statusCode;
	private String message;
	private transient Object data;

	public GenericResponse(int code, String msg, Object data) {
		statusCode = code;
		message = msg;
		this.data = data;
	}
}