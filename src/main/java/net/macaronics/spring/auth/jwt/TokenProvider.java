package net.macaronics.spring.auth.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
@Getter
public class TokenProvider implements InitializingBean{

	private static final String AUTHORITIES_KEY ="auth";
	
	private final String secret;
	
	private final long tokenValidityInMilliseconds;
	
	private Key key;

	/**
	 * 빈이 생성이 되고 주입을 받은 후에 secret 값을 Base64 Decode 해서 key 변수에 할당
	 */
	public TokenProvider(
			@Value("${jwt.secret}")  String secret,			
			@Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds
			) {
		this.secret=secret;
		this.tokenValidityInMilliseconds=tokenValidityInMilliseconds * 1000;
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes=Decoders.BASE64.decode(secret);
		this.key =Keys.hmacShaKeyFor(keyBytes);
	}
	
	
	

	/** 토큰 생성 
	 * application.yml 에서 설정했던 만료시간을 설정하고 토큰 생성
	 * */
	public String createToken(Authentication authentication) {
		String authorities =authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));		
		log.info("createToken  authorities   - {} " , authorities );
		
		long now =(new Date()).getTime();
		Date validity =new Date(now + this.tokenValidityInMilliseconds);
		
		 return Jwts.builder()
		         .setSubject(authentication.getName())
		         .claim(AUTHORITIES_KEY, authorities)
		         .signWith(key, SignatureAlgorithm.HS512)
		         .setExpiration(validity)
		         .compact();	
	}
	
	
	
	/**
	   Token에 남겨 있는 정보를 이용해 Authentication 객체를 리턴하는 메소드 생성
	 */
	public Authentication getAuthentication(String token){
		
		//토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 Authentication 객체를 리턴
		Claims claims= Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		//토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 Authentication 객체를 리턴
		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		
		
		User principal=new User(claims.getSubject(),   "", authorities);
		
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}
	
	
	//토큰의 유효성 검증을 수행하는 validateToken 메소드 추가
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		}catch (SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");	
		}catch(ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		}catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 서명입니다.");
		}catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}
	

	
	
	
	
	
	
}
