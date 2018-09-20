package ro.gss.proxy.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.gss.proxy.config.SecurityUtils;
import ro.gss.proxy.config.UserInfo;

@RestController
public class SecurityRest {
	
	@GetMapping("/principal")
	public UserInfo getPrincipal() {
		return SecurityUtils.getCurrentUser();
	}
	
}
