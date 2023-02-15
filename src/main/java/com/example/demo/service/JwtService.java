package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entities.JwtRequest;
import com.example.demo.entities.JwtResponse;

public interface JwtService {
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception;


}
