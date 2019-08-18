package com.example.starwarsapi.model;

public class Response {

	private int    code;
	private String message;
	
	public Response(int code, String mensage) {
		this.code = code;
		this.message = mensage;
	}

	public int getCode() {
		return this.code;
	}

	public String getMensage() {
		return this.message;
	}
}
