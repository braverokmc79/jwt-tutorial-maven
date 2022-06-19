package me.slivernine.tutorial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloController {

	@GetMapping("/hello")
	public ResponseEntity<String > hello(){
		log.info("hello");
		return ResponseEntity.ok("hello");
	}
	
}
