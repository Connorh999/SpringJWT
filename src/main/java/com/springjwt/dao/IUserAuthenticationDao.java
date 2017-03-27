package com.springjwt.dao;

import com.springjwt.model.User;

public interface IUserAuthenticationDao {

	public User getUserByEmail(String email);
}
