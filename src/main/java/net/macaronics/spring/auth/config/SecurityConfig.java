package net.macaronics.spring.auth.config;

import net.macaronics.spring.auth.jwt.JwtAccessDeniedHandler;
import net.macaronics.spring.auth.jwt.JwtAuthenticationEntryPoint;
import net.macaronics.spring.auth.jwt.JwtSecurityConfig;
import net.macaronics.spring.auth.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) //메소드 단위로 사용하기 위해 추가
public class SecurityConfig {
	
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {		

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			
		http			
			.csrf().disable()  //1.토큰을 사용해서 인증 처리하기때문에 csrf 설정이 disable 

			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint) //자격증명을 제공하지 않고 접근하려 할때 401 Unauthorized 에러를 리턴
			.accessDeniedHandler(jwtAccessDeniedHandler)  // 403 Forbidden 에러
			
			.and().headers().frameOptions().sameOrigin() //2. iframe 동일한 도메인 허용
		
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 3.세션을 사용하지 않는다.
				
			.and()		
			.authorizeRequests()
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers("/api/hello").permitAll()
			.antMatchers("/api/join").permitAll()
			.antMatchers("/api/authenticate").permitAll()
			.antMatchers("/api/signup").permitAll()
			.antMatchers("/web/**").permitAll()
			.anyRequest().authenticated()

			//.and().csrf().ignoringAntMatchers("/h2-console/**")	//h2-console csrf 적용안함 
           
			.and()
			.apply(new JwtSecurityConfig(tokenProvider));
				
		return http.build();
	}

	
	
}
