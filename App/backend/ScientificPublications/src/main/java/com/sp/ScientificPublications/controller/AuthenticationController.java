package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.security.ChangePasswordRequestDTO;
import com.sp.ScientificPublications.dto.security.LoginRequestDTO;
import com.sp.ScientificPublications.dto.UserDTO;
import com.sp.ScientificPublications.service.logic.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authService;
	
	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequest) {
		return new ResponseEntity<>(authService.login(loginRequest.getEmail(), loginRequest.getPassword()),
				HttpStatus.OK);
	}
	
	@PostMapping("/change_password")
	@Secured({"ROLE_AUTHOR", "ROLE_EDITOR"})
	public ResponseEntity<HttpStatus> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequest) {
		authService.changePassword(changePasswordRequest.getNewPassword(), changePasswordRequest.getOldPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
