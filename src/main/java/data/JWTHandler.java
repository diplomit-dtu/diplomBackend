package data;


import java.security.Key;
import java.util.Calendar;

import com.fasterxml.jackson.databind.ObjectMapper;

import data.dbDTO.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTHandler {
		private static final int TOKEN_EXPIRY = 10;

		@SuppressWarnings("serial")
		public static class AuthException extends Exception {
			public AuthException(String string) {
				super(string);
			}

	}

		static Key key = MacProvider.generateKey(SignatureAlgorithm.HS512);
		
	public static String generateJwtToken(User user){
		Calendar expiry = Calendar.getInstance();
		expiry.add(Calendar.SECOND, TOKEN_EXPIRY);
		return Jwts.builder()
				.setIssuer("DiplomIt")
				.claim("user", user)
				.signWith(SignatureAlgorithm.HS512, key)
				.setExpiration(expiry.getTime())
				.compact();
	}
	
	public static Jws<Claims> validateToken(String tokenString) throws AuthException {
		 Claims claims = null;
		try {
			claims = Jwts.parser().setSigningKey(key).parseClaimsJws(tokenString).getBody();
			 ObjectMapper mapper = new ObjectMapper();
			 User user = mapper.convertValue((claims.get("user")), User.class);
			 System.out.println(user);
			 return Jwts.parser().setSigningKey(key).parseClaimsJws(tokenString);
		} catch (ExpiredJwtException e) {
			throw new AuthException("Token too old!");
		} catch (UnsupportedJwtException e) {
			throw new AuthException("UnsupportedToken");
		} catch (MalformedJwtException e) {
			throw new AuthException("Malformed Token");
		} catch (SignatureException e) {
			throw new AuthException("Token invalid");
		} catch (IllegalArgumentException e) {
			throw new AuthException("Illegal Argument");
		}
		
			

	}
	

}
