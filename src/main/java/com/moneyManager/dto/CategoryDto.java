package com.moneyManager.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {

	private Long id;
	
	private String name;
	
	private String type;
	
	private String icon;
	
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Long profileId;
}
