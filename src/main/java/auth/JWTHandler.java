package auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import config.DeployConfig;
import data.dbDTO.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Calendar;

public class JWTHandler {
	private static final int TOKEN_EXPIRY = 960;

	public static class AuthException extends Exception {
		public AuthException(String string) {
			super(string);
		}

	}
	public static class ExpiredLoginException extends Exception {
		public ExpiredLoginException(String string) { super(string);
		}
	}
	public JWTHandler(){
		if (DeployConfig.JWT_SECRET_KEY != null && DeployConfig.JWT_SECRET_KEY !="") {
			String string = DeployConfig.JWT_SECRET_KEY;
			key = new SecretKeySpec(string.getBytes(),0,string.length(),"HS512");
		} else {
			key = MacProvider.generateKey(SignatureAlgorithm.HS512);
		}
	}
	private Key key;

	public String generateJwtToken(User user){
		user.setActiveAgenda(null);
		user.setAgendaInfoMap(null);
		user.setAdmins(null);
		user.setRightsGroups(null);
		user.setGeneralLinks(null);
		user.setViewers(null);
		Calendar expiry = Calendar.getInstance();
		expiry.add(Calendar.MINUTE, TOKEN_EXPIRY);
		return Jwts.builder()
				.setIssuer("DiplomIt")
				.claim("user", user)
				.signWith(SignatureAlgorithm.HS512, key)
				.setExpiration(expiry.getTime())
				.compact();
	}

	public Jws<Claims> validateToken(String tokenString) throws AuthException, ExpiredLoginException {
		Claims claims = null;
		try {
			System.out.println(tokenString);
			claims = Jwts.parser().setSigningKey(key).parseClaimsJws(tokenString).getBody();
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(claims.get("user"));
			User user = mapper.convertValue((claims.get("user")), User.class);
			System.out.println(user);
			return Jwts.parser().setSigningKey(key).parseClaimsJws(tokenString);
		} catch (ExpiredJwtException e) {
			throw new ExpiredLoginException("Token too old!");
		} catch (UnsupportedJwtException e) {
			throw new AuthException("UnsupportedToken");
		} catch (MalformedJwtException e) {
			throw new AuthException("Malformed Token");
		} catch (SignatureException e) {
			throw new AuthException("Token signature invalid");
		} catch (IllegalArgumentException e) {
			throw new AuthException("Illegal Argument: " + e.getMessage());
		}



	}


}
