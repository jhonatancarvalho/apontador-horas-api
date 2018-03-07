package br.com.jhonatan.apontadorhorasapi.security;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generateToken(String login) {
		return Jwts.builder()
				.setSubject(login)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	public boolean isValidToken(String token) {
		final Claims claims = getClaims(token);
		if (Objects.isNull(claims)) {
			return false;
		}
		
		final String username = claims.getSubject();
		final Date expirationDate = claims.getExpiration();
		final Date now = new Date(System.currentTimeMillis());
		
		return Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate);
	}
	
	public String getUsername(String token) {
		final Claims claims = getClaims(token);
		if (Objects.isNull(claims)) {
			return null;
		}
		
		return claims.getSubject();
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}	
	}
	
}
