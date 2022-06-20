package me.slivernine.tutorial.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import lombok.RequiredArgsConstructor;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		http
			.authorizeRequests()
			.antMatchers("/api/hello" ,"/h2-console/**").permitAll()
			.anyRequest().authenticated()

			.and().csrf().ignoringAntMatchers("/h2-console/**")			//h2-console csrf 적용안함 
            .and().headers().frameOptions().sameOrigin();//iframe 동일한 도메인 허용
		
		
	}

	
	
}
