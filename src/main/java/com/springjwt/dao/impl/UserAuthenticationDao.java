package com.springjwt.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springjwt.dao.IUserAuthenticationDao;
import com.springjwt.model.User;

@Repository
public class UserAuthenticationDao implements IUserAuthenticationDao {

	@Autowired
	private final SessionFactory sessionFactory;

	public UserAuthenticationDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public User getUserByEmail(String email) {
		return (User) sessionFactory.getCurrentSession()
				.createCriteria(User.class)
				.add(Restrictions.eq("email", email))
				.uniqueResult();
	}

}
