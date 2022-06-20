package net.macaronics.spring.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/web")
public class HomeController {

    @GetMapping("/hello")
    @ResponseBody
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

}
