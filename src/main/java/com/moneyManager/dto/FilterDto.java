package com.moneyManager.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FilterDto {

	private String keyword;
	
	private String type;
	
	private String sortField;
	
	private String sortOrder;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
}
