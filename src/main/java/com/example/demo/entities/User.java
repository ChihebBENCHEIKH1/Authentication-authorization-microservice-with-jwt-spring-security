package com.example.demo.entities;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor @Data @AllArgsConstructor
public class User {
	@Id
	private String userName;
	private String password;
	@ManyToMany
	@JoinTable(name = "user_role"
	,
	joinColumns = {
			@JoinColumn(name="user_id")
	},
	inverseJoinColumns = {
			@JoinColumn(name="role_id")
	}
			)
	private Collection<Role> roles;
	
}
