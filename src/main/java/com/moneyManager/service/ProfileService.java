package com.moneyManager.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moneyManager.dto.AuthDto;
import com.moneyManager.dto.ProfileDto;
import com.moneyManager.entity.ProfileEntity;
import com.moneyManager.repository.ProfileRepository;
import com.moneyManager.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	
	public ProfileDto registerProfile(ProfileDto dto) {
		
		ProfileEntity newProfile = toEntity(dto);	
		newProfile = profileRepository.save(newProfile);
		
		return toDTO(newProfile);
	}
	
	private ProfileDto toDTO(ProfileEntity entity) {
		return ProfileDto.builder()
				.email(entity.getEmail())
				.fullName(entity.getFullName())
				.profileImgUrl(entity.getProfileImgUrl())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.password(null)
				.build();
	}
	
	private ProfileEntity toEntity(ProfileDto dto) {
		return ProfileEntity.builder()
				.email(dto.getEmail())
				.fullName(dto.getFullName())
				.password(passwordEncoder.encode(dto.getPassword()))
				.profileImgUrl(dto.getProfileImgUrl())
				.createdAt(dto.getCreatedAt())
				.updatedAt(dto.getUpdatedAt())
				.build();
	}
	
	
	public ProfileEntity getCurrentProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return profileRepository.findByEmail(authentication.getName())
				.orElseThrow(()-> new UsernameNotFoundException("Profile not found with this mail : " + authentication.getName()));
	}
	
	public ProfileDto getPublicProfile(String email) {
		ProfileEntity currentProfile = null;
		if(email == null)
			currentProfile = getCurrentProfile();
		else {
			currentProfile = profileRepository.findByEmail(email)
					.orElseThrow(()-> new UsernameNotFoundException("Profile not found with this mail : " + email));
		}
		return ProfileDto.builder()
				.fullName(currentProfile.getFullName())
				.createdAt(currentProfile.getCreatedAt())
				.updatedAt(currentProfile.getUpdatedAt())
				.email(currentProfile.getEmail())
				.profileImgUrl(currentProfile.getProfileImgUrl())
				.build();
	}

	public Map<String, Object> authenticateAndGenerateToken(AuthDto authDto) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(),authDto.getPassword()));
			String token = jwtUtil.generateToken(authDto.getEmail());
			return Map.of(
					"token",token,
					"user",getPublicProfile(authDto.getEmail())
					);
		}catch(Exception e) {
			throw new RuntimeException("Invalid Email Or Password");
		}
	}
}
