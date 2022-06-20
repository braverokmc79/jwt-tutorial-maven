package me.slivernine.tutorial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import me.slivernine.tutorial.entity.User;


public interface UserRepository extends JpaRepository<User, Long>{

	//username 을 기준으로 User 정보를 가져올때 권한 정보도 같이 가져온다.
	//@EntityGraph 은 쿼리가 수행이 될때 Lazy 조회가 아니고 Eager 조회로 authorities정보를 같이 가져온다.
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByUsername(String username);	
}
