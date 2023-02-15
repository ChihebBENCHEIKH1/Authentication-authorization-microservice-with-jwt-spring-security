package com.example.demo.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JwtService;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;
	@Autowired
	private JwtUtil jwtUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
		String jwtToken=null;
		String userName=null;
		final String header=request.getHeader("Authorization");
		if(header!=null && header.startsWith("Bearer "))
		{
			jwtToken=header.substring(7);
			try {
				userName=jwtUtil.getUsernameFromToken(jwtToken);
			}
			catch(IllegalArgumentException e)
			{
				System.out.println("illegal argument");
			}
			catch(ExpiredJwtException e)
			{
				System.out.println("Expired token");
			}
			
		}
		else
		{
			System.out.print("JWT token doesn't start with Bearer");
		}
		if(userName!=null&& SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails details=jwtService.loadUserByUsername(userName);
			if(jwtUtil.validateToken(jwtToken, details));
			{
				UsernamePasswordAuthenticationToken userAuthenticationToken= new UsernamePasswordAuthenticationToken(details,null, details.getAuthorities());
				userAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userAuthenticationToken);
			}
		}
	filterChain.doFilter(request, response);
	}

}
