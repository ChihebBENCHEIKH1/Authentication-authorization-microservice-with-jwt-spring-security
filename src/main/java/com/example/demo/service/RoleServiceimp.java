package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.RoleRepository;
import com.example.demo.entities.Role;

@Service
@org.springframework.transaction.annotation.Transactional
public class RoleServiceimp implements RoleService{

	@Autowired
	private RoleRepository repository;
	@Override
	public Role addRole(Role role) {
		return repository.save(role);
	}

}
