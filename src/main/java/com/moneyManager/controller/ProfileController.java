package com.moneyManager.controller;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moneyManager.dto.AuthDto;
import com.moneyManager.dto.ProfileDto;
import com.moneyManager.service.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping("/register")
	public ResponseEntity<?> registerProfile(@RequestBody ProfileDto dto){
		return ResponseEntity.ok(profileService.registerProfile(dto));
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDto authDto){
		try {
			Map<String, Object> response = profileService.authenticateAndGenerateToken(authDto);
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message",e.getMessage()));
		}
	}
	
	@GetMapping("/profile")
	public ResponseEntity<ProfileDto> getCurrentProfileDetails() {
		ProfileDto profile = profileService.getPublicProfile(null);
		return ResponseEntity.status(HttpStatus.OK).body(profile);
	}
}
