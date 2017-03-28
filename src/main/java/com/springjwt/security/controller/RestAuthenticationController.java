package com.springjwt.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springjwt.dto.UserAuthenticationDto;
import com.springjwt.security.model.JwtAuthenticationRequest;
import com.springjwt.security.model.JwtAuthenticationResponse;
import com.springjwt.security.util.JwtTokenUtils;
import com.springjwt.service.impl.UserAuthenticationService;

@RestController
@RequestMapping(value = "${jwt.route.authentication.path}")
public class RestAuthenticationController {
	
	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtils tokenUtils;
	
	@Autowired
	UserAuthenticationService userAuthService;

	@RequestMapping(value = "${jwt.route.authentication.login}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, 
			Device device) {
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authenticationRequest.getEmail(), 
						authenticationRequest.getPassword()
				)
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		final UserDetails userDetails = userAuthService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = tokenUtils.generateToken(userDetails, device);
		
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
	}
	
	@RequestMapping(value = "${jwt.route.authentication.refresh}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		String email = tokenUtils.getEmailFromToken(token);
		UserAuthenticationDto user = (UserAuthenticationDto) userAuthService.loadUserByUsername(email);
		
		if (tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset())) {
			String refreshed = tokenUtils.refreshToken(token);
			return ResponseEntity.ok(new JwtAuthenticationResponse(refreshed));
		}
		else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
}
