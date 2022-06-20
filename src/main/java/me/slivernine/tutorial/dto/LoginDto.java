package me.slivernine.tutorial.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 로그인 시 사용할  LoginDto
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDto {

	
	@NotNull
	@Size(min=3, max=50)
	private String username;
	
	@NotNull
	@Size(min=3, max=100)
	private String password;
	

}

