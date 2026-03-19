package com.moneyManager.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
	private String fullName;
	
	private String email;
	
	private String password;
	
	private String profileImgUrl;
	
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
