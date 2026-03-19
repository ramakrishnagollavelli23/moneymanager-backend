package com.moneyManager.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyManager.dto.ExpenseDto;
import com.moneyManager.dto.FilterDto;
import com.moneyManager.dto.IncomeDto;
import com.moneyManager.service.ExpenseService;
import com.moneyManager.service.IncomeService;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {
	
	private final ExpenseService expenseService;
	private final IncomeService incomeService;
	
	@PostMapping()
	public ResponseEntity<?> getFilteredData(@RequestBody FilterDto filterDto){
		LocalDate startDate = filterDto.getStartDate() != null ? filterDto.getStartDate() : LocalDate.MIN;
		LocalDate endDate = filterDto.getEndDate() != null ? filterDto.getEndDate() :LocalDate.now();
		String keyword = filterDto.getKeyword() != null ? filterDto.getKeyword() : "";
		String sortField = filterDto.getSortField() != null ? filterDto.getSortField() : "date";
		Sort.Direction sortOrder = "desc".equalsIgnoreCase(filterDto.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Sort sort = Sort.by(sortOrder,sortField);
		
		if("income".equalsIgnoreCase(filterDto.getType())) {
			List<IncomeDto> incomeList = incomeService.getFilterData(startDate, endDate, keyword, sort);
			return ResponseEntity.status(HttpStatus.OK).body(incomeList);
		}
		else if("expense".equalsIgnoreCase(filterDto.getType())) {
			List<ExpenseDto> expenseList = expenseService.getFilterData(startDate, endDate, keyword, sort);
			return ResponseEntity.status(HttpStatus.OK).body(expenseList);
		}
		else {
			return ResponseEntity.badRequest().body("Invalid type, it must 'expense' or 'income'.");
		}
	}
}
