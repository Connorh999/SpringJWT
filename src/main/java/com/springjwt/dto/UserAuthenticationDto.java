package com.springjwt.dto;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springjwt.model.Authority;
import com.springjwt.model.User;

public class UserAuthenticationDto implements UserDetails {

	private int id;
	private String email;
	private String password;
	private Date lastPasswordReset;
	private Collection<? extends GrantedAuthority> authorities;
	private String firstName;
	private String lastName;
	
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	
	public UserAuthenticationDto() {}

	public UserAuthenticationDto(User user) {
		setId(user.getId());
		setUsername(user.getEmail());
		setPassword(user.getPassword());
		setLastPasswordReset(user.getLastPasswordReset());
		setAuthorities(user.getAuthorities());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setAccountNonExpired(true);
		setAccountNonLocked(true);
		setCredentialsNonExpired(true);
		setEnabled(user.getEnabled());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public void setUsername(String email) {
		this.email = email;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public Date getLastPasswordReset() {
		return lastPasswordReset;
	}

	public void setLastPasswordReset(Date lastPasswordReset) {
		this.lastPasswordReset = lastPasswordReset;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities.stream()
				.map(auth -> new SimpleGrantedAuthority(
						"ROLE_" + auth.getName()
				)).collect(Collectors.toSet());
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
	
	@JsonIgnore
	public boolean getAccountNonExpired() {
		return accountNonExpired;
	}
	
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return getAccountNonExpired();
	}

	@JsonIgnore
	public boolean getAccountNonLocked() {
		return accountNonLocked;
	}
	
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return getAccountNonLocked();
	}

	@JsonIgnore
	public boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return getCredentialsNonExpired();
	}

	@JsonIgnore
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean isEnabled() {
		return getEnabled();
	}
}
