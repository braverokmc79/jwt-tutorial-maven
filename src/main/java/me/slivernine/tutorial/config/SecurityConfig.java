package me.slivernine.tutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.slivernine.tutorial.jwt.JwtAccessDeniedHandler;
import me.slivernine.tutorial.jwt.JwtAuthenticationEntryPoint;
import me.slivernine.tutorial.jwt.JwtSecurityConfig;
import me.slivernine.tutorial.jwt.TokenProvider;



@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) //메소드 단위로 사용하기 위해 추가
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
//	public SecurityConfig(
//			TokenProvider tokenProvider,
//			JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
//			JwtAccessDeniedHandler jwtAccessDeniedHandler
//			) {
//		this.tokenProvider=tokenProvider;
//		this.jwtAuthenticationEntryPoint=jwtAuthenticationEntryPoint;
//		this.jwtAccessDeniedHandler=jwtAccessDeniedHandler;
//	}
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		http			
			.csrf().disable()  //1.토큰을 사용해서 인증 처리하기때문에 csrf 설정이 disable 

			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler)
			
			.and().headers().frameOptions().sameOrigin() //2. iframe 동일한 도메인 허용
		
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 3.세션을 사용하지 않는다.
				
			.and()		
			.authorizeRequests()
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers("/api/hello").permitAll()
			.antMatchers("/api/join").permitAll()
			.antMatchers("/api/authenticate").permitAll()
			.antMatchers("/api/signup").permitAll()			
			.anyRequest().authenticated()

			//.and().csrf().ignoringAntMatchers("/h2-console/**")	//h2-console csrf 적용안함 
           
			.and()
			.apply(new JwtSecurityConfig(tokenProvider));
				
	}


	
	
}
