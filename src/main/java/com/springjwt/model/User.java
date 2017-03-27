package com.springjwt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * User entity for storing mapped user details from the database.
 * 
 * @author Connor Hanson
 * @since 2/25/17
 * @version 1.0
 */
@Entity
@Table(name = "Users")
public class User {

	@Id
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastPasswordReset;
	private Boolean enabled;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "UsersAuthorities",
		joinColumns = {
			@JoinColumn(name = "userId", referencedColumnName = "id")
		},
		inverseJoinColumns = {
			@JoinColumn(name = "authorityId", referencedColumnName = "id")
		}
	)
	private Set<Authority> authorities = new HashSet<>();

	public User() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastPasswordReset() {
		return lastPasswordReset;
	}
	
	public void setLastPasswordReset(Date lastPasswordReset) {
		this.lastPasswordReset = lastPasswordReset;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

}