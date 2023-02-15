package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.JwtRequest;
import com.example.demo.entities.JwtResponse;
import com.example.demo.entities.User;
import com.example.demo.service.JwtServiceImp;

import lombok.var;


@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class JwtController {
	@Autowired
	private JwtServiceImp jwtService;
	
	@PostMapping(path="/authenticate")
	public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		return jwtService.createJwtToken(jwtRequest);
	}
	@PostMapping(path="/register")
	public User register(@RequestBody User user) throws Exception
	{
		return jwtService.registerUser(user);
	}
}
