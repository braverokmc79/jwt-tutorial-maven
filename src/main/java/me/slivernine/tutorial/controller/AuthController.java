package me.slivernine.tutorial.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.slivernine.tutorial.dto.LoginDto;
import me.slivernine.tutorial.dto.TokenDto;
import me.slivernine.tutorial.entity.User;
import me.slivernine.tutorial.jwt.JwtFilter;
import me.slivernine.tutorial.jwt.TokenProvider;
import me.slivernine.tutorial.repository.UserRepository;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
//	public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
//		this.tokenProvider=tokenProvider;
//		this.authenticationManagerBuilder=authenticationManagerBuilder;
//	}
	
	
	@PostMapping("/join")
	public String join(@RequestBody User user) {
		log.info("회원 가입 파라미터 : {} " , user.toString());
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActivtated(true);
		userRepository.save(user);
		return "회원가입완료";
	}
	
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto){
		log.info("0.authenticate : {} " , loginDto.toString());
		
		//1.아이디 비밀번호로 authenticationToken  생성
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
		
		log.info("1.authenticationToken : {} " , authenticationToken.toString());
		
		//2. authentication 생성 - 아이디 비번이 틀릴경우   requestURI: /error 
		Authentication authentication=authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		
		log.info("2. authentication 생성 : {} " , authentication);
	
		//3.세션 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);		
		//4.토큰 생성
		String jwt =tokenProvider.createToken(authentication);
		
		log.info("4.토큰 생성 : {} " , jwt);
		
	
		
		//5. 데이터 반환		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " +jwt);
			
		return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);	
	}
	
	/**
	 * 
	 * var jsonData =JSON.parse(responseBody(
	 * pm.globals.set("jwt_tutorial_token", jsonData.token);
	 * 
	 * 
	 */
	
	
	
	
}
