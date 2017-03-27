package com.springjwt.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springjwt.security.util.JwtTokenUtils;
import com.springjwt.service.impl.UserAuthenticationService;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	//Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserAuthenticationService userAuthService;
	
	@Autowired
	private JwtTokenUtils tokenUtils;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader(tokenHeader);
		//token.startsWith("Bearer ");
		//token = token.substring(7);
		String email = tokenUtils.getEmailFromToken(token);
		
		//logger.info("Checking authentication for user: " + email);
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userAuthService.loadUserByUsername(email);
			if (tokenUtils.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken auth = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//logger.info("Authentication user: " + email + "; setting security context.");
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
