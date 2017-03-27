package com.springjwt.security.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.springjwt.dto.UserAuthenticationDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtils {

	private final String AUDIENCE_UNKNOWN = "unknown";
	private final String AUDIENCE_WEB = "web";
	private final String AUDIENCE_MOBILE = "mobile";
	private final String AUDIENCE_TABLET = "tablet";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private long expiration;

	public String getEmailFromToken(String token) {
		String email;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			email = claims.getSubject();
		} catch (Exception e) {
			email = null;
		}
		
		return email;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((long) claims.get("created"));
		} catch (Exception e) {
			created = null;
		}
		
		return created;
	}
	
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		
		return expiration;
	}
	
	public String getAudienceFromToken(String token) {
		String audience;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			audience = claims.getAudience();
		} catch (Exception e) {
			audience = null;
		}
		
		return audience;
	}
	
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		
		try {
			claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}
		
		return claims;
	}
	
	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}
	
	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		
		return expiration.before(new Date());
	}

	private boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return lastPasswordReset != null && created.before(lastPasswordReset);
	}
	
	private String generateAudience(Device device) {
		String audience = AUDIENCE_UNKNOWN;
		
		if (device != null) {
			if (device.isNormal()) {
				audience = AUDIENCE_WEB;
			}
			else if (device.isTablet()) {
				audience = AUDIENCE_TABLET;
			}
			else if (device.isMobile()) {
				audience = AUDIENCE_MOBILE;
			}
		}
		
		return audience;
	}
	
	private boolean ignoreTokenExpiration(String token) {
		String audience = getAudienceFromToken(token);
		
		return audience.equals(AUDIENCE_TABLET) || audience.equals(AUDIENCE_MOBILE);
	}
	
	public String generateToken(UserDetails userDetails, Device device) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", userDetails.getUsername());
		claims.put("aud", generateAudience(device));
		claims.put("created", new Date());
		
		return generateToken(claims);
	}
	
	private String generateToken(Map<String, Object> claims) {
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	public boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}
	
	public String refreshToken(String token) {
		String refreshed;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put("created", new Date());
			refreshed = generateToken(claims);
		} catch (Exception e) {
			refreshed = null;
		}
		
		return refreshed;
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		UserAuthenticationDto user = (UserAuthenticationDto) userDetails;
		final String email = getEmailFromToken(token);
		final Date created = getCreatedDateFromToken(token);
		//final Date expiration = getExpirationDateFromToken(token);
		
		return email.equals(user.getUsername()) && !isTokenExpired(token) 
				&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordReset());
	}
	
}
