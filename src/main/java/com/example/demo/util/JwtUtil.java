package com.example.demo.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private static final String SECRET_KEY="mySecret1234";
	private static final int TOKEN_VALIDITY = 3600*5;
	public String getUsernameFromToken(String token)
	{
		return getClaimFromToken(token, Claims::getSubject);
		
	}
	private <T> T getClaimFromToken(String token,Function<Claims, T> claimResolver)
	{
		final Claims claims=getAllClaims(token);
		return claimResolver.apply(claims);
	}
	@SuppressWarnings("deprecation")
	private Claims getAllClaims(String token)
	{
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	public boolean validateToken(String token,UserDetails details)
	{
		String userName=getUsernameFromToken(token);
		return (userName.equals(details.getUsername())&& !isTokenExpired(token));
	}
	private boolean isTokenExpired(String token)
	{
		final Date expirationDate=getExpirationdate(token);
		return expirationDate.before(new Date());
	}
	private Date getExpirationdate(String token)
	{
		return getClaimFromToken(token, Claims::getExpiration);
	}
	@SuppressWarnings("deprecation")
	public String generateToken(UserDetails details)
	{
	
		return Jwts.builder()
				.claim("roles",details.getAuthorities().stream().map(x->(String)x.getAuthority()).collect(Collectors.toList()))
				.setSubject(details.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+TOKEN_VALIDITY*1000))
				.signWith(SignatureAlgorithm.HS256,SECRET_KEY)
				.compact();
	}
}
