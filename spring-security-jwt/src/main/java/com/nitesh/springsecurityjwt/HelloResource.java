package com.nitesh.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nitesh.springsecurityjwt.mode.AuthenticationRequest;
import com.nitesh.springsecurityjwt.mode.AuthenticationResponse;
import com.nitesh.springsecurityjwt.services.MyUserDetailService;
import com.nitesh.springsecurityjwt.util.JwtUtil;

@RestController
public class HelloResource {
	
	@Autowired
	public AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@GetMapping({"/hello"})
	public String hello(){
		return "Hello-World";
	}
	
	@PostMapping(value="/authenticate")
	public ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
					authenticationRequest.getPassword()));
			
			} catch (BadCredentialsException e) {
					throw new Exception("Incorrect username or password",e);
		 }
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUserName());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
