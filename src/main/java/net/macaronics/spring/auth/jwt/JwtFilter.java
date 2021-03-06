package net.macaronics.spring.auth.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * GenericFilterBean 을 extends 해서  doFilter Override,
 * 실제 필터링 로직은 doFilter 내부에 작성
 *
 */
@Slf4j
public class JwtFilter extends GenericFilterBean{
	
	
	public static final String AUTHORIZATION_HEADER = "Authorization";

	private TokenProvider tokenProvider;
	
	public JwtFilter(TokenProvider tokenProvider) {
		this.tokenProvider=tokenProvider;
		
	}
	
	
	/** doFilter 는 토큰의 인증정보를 SecurityContext 에 저장하는 역할 수행 */
	@Override	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest=(HttpServletRequest) request;
		String jwt =resolveToken(httpServletRequest);
		String requestURI=httpServletRequest.getRequestURI();
		
		log.debug("jwt: {} " , jwt);
		log.debug("requestURI: {} " , requestURI);
		
		if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
			Authentication authentication=tokenProvider.getAuthentication(jwt);
			//시큐리티 세션에 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Security Context 에  '{}' 인증 정보를 저장했습니다. uri: {} ",authentication.getName(), requestURI);
		}else {
			log.debug("유효한 JWT 토큰이 없습니다. uri : {} " , requestURI);
		}
		
		filterChain.doFilter(request, response);
	}

	
	/** Request Header 에서 토큰 정보를 꺼내기위한 resolveToken 메소드 추가 */
	private String resolveToken(HttpServletRequest request) {
		String bearerToken=request.getHeader(AUTHORIZATION_HEADER);
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	
	
	
	
}


