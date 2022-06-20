package me.slivernine.tutorial.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원가입시에 사용할 UesrDto
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UesrDto {


	
	@NotNull
	@Size(min=3, max=50)
	private String username;
	
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	@Size(min=3, max=100)
	private String password;
	
	
	@NotNull
	@Size(min=3, max=50)
	private String nickname;
	
	
	
}
