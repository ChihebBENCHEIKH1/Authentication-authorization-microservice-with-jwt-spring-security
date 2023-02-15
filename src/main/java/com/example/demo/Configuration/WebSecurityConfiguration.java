package com.example.demo.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.filters.JwtRequestFilter;
import com.example.demo.service.JwtService;
import com.example.demo.service.JwtServiceImp;
import com.example.demo.service.RoleService;
import com.example.demo.service.RoleServiceimp;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImp;
import com.example.demo.util.JwtUtil;

import feign.Feign;

@Configuration
@EnableWebSecurity  
@EnableGlobalMethodSecurity (prePostEnabled = true)//les @ hethom bch kn method emo3ayna test7a9 authentification l role mo3ayen
//admin par exp
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsService jwtService;
	@Autowired
	private JwtRequestFilter filter;
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
	    return authentication -> {
	        UserDetails userDetails = jwtService.loadUserByUsername(authentication.getName());
	        if (userDetails == null) {
	            System.out.print("Invalid username or password");
	        }
	        if (!encoder().matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
	        	System.out.print("Invalid username or password");
	        }
	        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	    };
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf().disable()
		.authorizeRequests().antMatchers("/authenticate")
		.permitAll()
		.antMatchers(HttpHeaders.ALLOW)
		.permitAll()
		.anyRequest().authenticated()
		.and()
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
	}
	@Bean
	public PasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
	@Bean
	public UserDetailsService detail()
	{
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	 @Bean 
	 public JwtRequestFilter filter()
	 {
		 return new JwtRequestFilter();
	 }
	 @Bean 
	 public JwtService service()
	 {
		 return new JwtServiceImp();
	 }
	 @Bean 
	 public JwtUtil util()
	 {
		 return new JwtUtil();
	 }
	 @Bean
	 public RoleService roleService()
	 {
		 return new RoleServiceimp();
	 }

	//juste bch tconfigury l passwordEncoder maa l jwtService
	@Autowired
	public void globalConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
	{
		authenticationManagerBuilder.userDetailsService(jwtService).passwordEncoder(encoder());
		
	}
}
