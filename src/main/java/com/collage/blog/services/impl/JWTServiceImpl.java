package com.collage.blog.services.impl;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.collage.blog.services.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService{

	
	String secretKey = "";
	
	public JWTServiceImpl() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
			System.out.println(secretKey);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		}
	}
	
	@Override
	public String generateToken(String userName) {
		
		Map<String,Object> claims = new HashMap<>();
		
		String token =
				Jwts.builder()
				.claims()
				.add(claims)
				.subject(userName)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+60*30*30*30))
				.and()
				.signWith(getKey())
				.compact();
		
		System.out.println(token);

		return token;
	}

	private SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		System.out.println(Keys.hmacShaKeyFor(keyBytes));
		
		return Keys.hmacShaKeyFor(keyBytes);
	}

	 public String extractUserName(String token) {
	        
		   String userName = extractClaim(token, Claims::getSubject);
	        return userName;
	    }

	    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
	        final Claims claims = extractAllClaims(token);
	        System.out.println(claims);
	        return claimResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	    	System.out.println(token);
	    	Claims claims =  Jwts.parser()
	                .verifyWith(getKey())
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	    	System.out.println(claims);
	    	
	    	return claims;
	    }

	    public boolean validateToken(String token, UserDetails userDetails) {
	        final String userName = extractUserName(token);
	        
	        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }

	    private boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    private Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }
	

}
