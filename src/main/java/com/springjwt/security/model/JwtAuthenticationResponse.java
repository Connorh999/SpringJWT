package com.springjwt.security.model;

import java.io.Serializable;

public class JwtAuthenticationResponse implements Serializable {

	private static final long serialVersionUID = -7905436303211525941L;

	private final String token;
	
	public JwtAuthenticationResponse(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
}
