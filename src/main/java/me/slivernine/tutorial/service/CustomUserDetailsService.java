package me.slivernine.tutorial.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import me.slivernine.tutorial.entity.User;
import me.slivernine.tutorial.repository.UserRepository;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository uesrRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.uesrRepository=userRepository;
	}
	
		
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return uesrRepository.findOneWithAuthoritiesByUsername(username)
				.map(user->createUser(username, user))
				.orElseThrow(()->new UsernameNotFoundException(username +" -> 데이터베이스에서 찾을 수 없습니다."));
	}
	
	
	private org.springframework.security.core.userdetails.User createUser(String username, User user) {
		if(!user.isActivated()) {
			throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
		}
		
		List<GrantedAuthority> grantedAuthorities =user.getAuthorities().stream()
				.map(authority-> new SimpleGrantedAuthority(authority.getAuthorityName()))
				.collect(Collectors.toList());
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);		
	}
	
	
	

}
