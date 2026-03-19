package com.moneyManager.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDto {

	private Long id;
	
	private String name;
	
	private String icon;
	
	private String categoryName;
	
	private Long categoryId;
	
	private BigDecimal amount;
	
	private LocalDate date;

	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
		
}