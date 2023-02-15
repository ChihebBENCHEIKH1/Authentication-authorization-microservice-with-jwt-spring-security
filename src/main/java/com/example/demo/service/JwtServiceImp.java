package com.example.demo.service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserRepository;
import com.example.demo.entities.JwtRequest;
import com.example.demo.entities.JwtResponse;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.util.JwtUtil;

@Service
@Transactional
public class JwtServiceImp implements UserDetailsService, JwtService{

	@Autowired
	private UserRepository repository;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private PasswordEncoder encoder;
	private AuthenticationManager authenticationManager;
	public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception
	{
		String username=jwtRequest.getUsername();
		String password=jwtRequest.getPassword();
		authenticate(username, password);
		final UserDetails details=loadUserByUsername(username);
		String generatedToken=jwtUtil.generateToken(details);
		User user=repository.findById(username).get();
		return new JwtResponse(user,generatedToken);
		
		
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=repository.findById(username).get();
		if(user!=null)
		{	
			return new org.springframework.security.core.userdetails.User(
					user.getUserName(),user.getPassword(),
					user.getRoles().stream().
					map(x->new SimpleGrantedAuthority("ROLE_"+x.getRoleName())).
					collect(Collectors.toList()));
			
		}
		else
		{
			throw new UsernameNotFoundException("User is not found!");
		}
	}
	private void authenticate(String name,String password)
	{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, password));
			System.out.println("********");
		}catch(DisabledException e)
		{
			System.out.println("disabled user!");
		}
		catch(BadCredentialsException e)
		{
			System.out.println("Bad credentials from user");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
	public User registerUser(User user)
	{
		ArrayList<Role> roles=new ArrayList<>();
		roles.add(new Role("USER","Default role"));
		user.setRoles(roles);
		user.setPassword(encoder.encode(user.getPassword()));
		return user;
		
	}

}
