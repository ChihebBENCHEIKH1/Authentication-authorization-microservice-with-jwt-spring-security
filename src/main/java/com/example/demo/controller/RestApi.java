package com.example.demo.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

//im

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;


@RestController
public class RestApi {
	private RoleService roleService;
	private UserService service;
	@Autowired
	public RestApi(RoleService roleService, UserService service) {
		this.roleService = roleService;
		this.service = service;
	}
	@GetMapping({"/forAdmin"})
	@PreAuthorize("hasRole('ADMIN')")
	public Object forAdmin()
	{
		return "ma7bee bik f espace l admin ";
	}
	@GetMapping({"/forUser"})
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public String forUser()
	{
		return "ma7bee bik f espace orange";
	}
	@PostConstruct
	public void initRoleAndUser()
	{
		service.addUser(new User("Mounir","1234", null));
	}
	
	@PostMapping({"/addRole"})
	public Role addRoleViaRest(@RequestBody Role role)
	{
		return roleService.addRole(role);
	}
	@PostMapping({"/addUser"})
	public User addUserViaRest(@RequestBody User user)
	{
		return service.addUser(user);
	}

}
