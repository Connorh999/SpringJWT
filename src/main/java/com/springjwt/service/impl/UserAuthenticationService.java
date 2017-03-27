package com.springjwt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springjwt.dao.IUserAuthenticationDao;
import com.springjwt.dto.UserAuthenticationDto;
import com.springjwt.model.User;

@Service
public class UserAuthenticationService implements UserDetailsService {

	private final IUserAuthenticationDao userAuthenticationDao;
	
	@Autowired
	public UserAuthenticationService(final IUserAuthenticationDao userAuthenticationDao) {
		this.userAuthenticationDao = userAuthenticationDao;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userAuthenticationDao.getUserByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("User with email: " + email + " does not exist.");
		}
		
		return new UserAuthenticationDto(user);
	}

}
