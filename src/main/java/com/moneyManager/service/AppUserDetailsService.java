package com.moneyManager.service;

import java.util.Collections;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moneyManager.entity.ProfileEntity;
import com.moneyManager.repository.ProfileRepository;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

	private final ProfileRepository profileRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		ProfileEntity existingProfile = profileRepository.findByEmail(email)
				.orElseThrow(()->new UsernameNotFoundException("Profile Not Found with this mail : " + email));
		
		return User.builder()
				.username(existingProfile.getEmail())
				.password(existingProfile.getPassword())
				.authorities(Collections.emptyList())
				.build();				
	}

}
