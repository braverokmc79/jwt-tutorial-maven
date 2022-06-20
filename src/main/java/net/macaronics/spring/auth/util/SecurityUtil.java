package net.macaronics.spring.auth.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class SecurityUtil {
	
	/**
	 * getCurrentUsername 메소드의 역할은 Security Context 의 Authentication 객체를 이용해
	 * username을 리턴해주는 간단한 유틸성 메소드입니다.
	 */
	public static Optional<String> getCurrentUsername() {
		// 시큐리티 세션정보에 있는 authentication 값 가져오기, 값이 없다면 인증된 정보가 없다
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			log.debug("Security Context에 인증 정보가 없습니다.");
			return Optional.empty();
		}

		String username = null;
		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
			username = springSecurityUser.getUsername();
		} else if (authentication.getPrincipal() instanceof String) {
			username = (String) authentication.getPrincipal();
		}

		return Optional.ofNullable(username);
	}
	
	
	
}