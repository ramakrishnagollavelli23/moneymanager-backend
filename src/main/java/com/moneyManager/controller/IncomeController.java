package com.moneyManager.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyManager.dto.IncomeDto;
import com.moneyManager.service.IncomeService;

@RestController
@RequestMapping("/income")
@RequiredArgsConstructor
public class IncomeController {

	private final IncomeService incomeService;
	
	@PostMapping()
	public ResponseEntity<IncomeDto> saveIncome(@RequestBody IncomeDto dto){
		IncomeDto newIncome = incomeService.toSaveIncome(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(newIncome);
	}
	
	@GetMapping()
	public ResponseEntity<List<IncomeDto>> getCurrentMonthIncomes(){
		List<IncomeDto> incomeList = incomeService.getCurrentMonthIncomesForCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(incomeList);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCurrentUserIncome(@PathVariable Long id){
		incomeService.deleteCurrentUserIncomeById(id);
		return ResponseEntity.noContent().build();
	}
}
