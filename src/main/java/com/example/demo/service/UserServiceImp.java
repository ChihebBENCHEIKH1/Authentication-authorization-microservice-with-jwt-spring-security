package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.dao.UserRepository;
import com.example.demo.entities.User;

public class UserServiceImp implements UserService {
	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder encoder;
	@Override
	public User addUser(User user) {
		user.setPassword(encodePassword(user.getPassword()));
		return repository.save(user);
	}
	private String encodePassword(String password)
	{
		return encoder.encode(password);
	}

}
