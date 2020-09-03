package org.easy.auth.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class DeoceanLoginEndPoint {

	@GetMapping("/auth/login")
	public String loginPage1() {
		return "login";
	}
	
	@GetMapping("/login")
	public String loginPage2() {
		return "login";
	}

}
