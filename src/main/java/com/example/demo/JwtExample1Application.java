package com.example.demo;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.filters.JwtRequestFilter;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImp;

import feign.Feign;

@EnableFeignClients
@SpringBootApplication(scanBasePackages={
"com.example.demo.Configuration", "com.example.demo.controller","com.example.demo.filters"})
public class JwtExample1Application {

	public static void main(String[] args) {
		SpringApplication.run(JwtExample1Application.class, args);
	}
	
	
	 @Bean
	    public UserService userService() {
	        return new UserServiceImp();
	    }
	
	 
	@Bean
	CommandLineRunner start(RoleService roleService,UserService service)
	{
		return args ->{
			roleService.addRole(new Role("USER","Default configuration"));
			roleService.addRole(new Role("ADMIN","ADMIN OF THE SYSTEM"));
			User user=new User("Sami","1234",new ArrayList<>());
			user.getRoles().add(new Role("USER","Default configuration"));
			service.addUser(user);
			User user1=new User("Sami","1234",new ArrayList<>());
			user1.getRoles().add(new Role("USER","Default configuration"));
			service.addUser(user1);
			User user2=new User("mohsen","1234",new ArrayList<>());
			user2.getRoles().add(new Role("ADMIN","ADMIN OF THE SYSTEM"));
			service.addUser(user2);
			
			
		};
	}
	

}
