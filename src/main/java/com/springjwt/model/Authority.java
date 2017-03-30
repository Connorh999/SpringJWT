package com.springjwt.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The authority entity for users.
 * 
 * @author Connor Hanson
 * @since 2/26/17
 * @version 1.0
 */
@Entity
@Table(name = "Authorities")
public class Authority {
    
	@Id
	private Integer id;
	private String name;
	
	public Authority() {}
	
	public Authority(Integer id, String name) {
		this.setId(id);
		this.setName(name);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
