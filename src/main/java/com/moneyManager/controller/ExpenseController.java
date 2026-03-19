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

import com.moneyManager.dto.ExpenseDto;
import com.moneyManager.service.ExpenseService;

@RestController
@RequestMapping("expense")
@RequiredArgsConstructor
public class ExpenseController {

	private final ExpenseService expenseService;
	
	@PostMapping()
	public ResponseEntity<ExpenseDto> saveExpense(@RequestBody ExpenseDto dto){
		ExpenseDto newExpense = expenseService.toSaveExpense(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(newExpense);
	}
	
	@GetMapping()
	public ResponseEntity<List<ExpenseDto>> getCurrentUserExpenses(){
		List<ExpenseDto> expenseLists = expenseService.getCurrentMonthExpenseForCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(expenseLists);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCurrentUserExpense(@PathVariable Long id){
		expenseService.deleteCurrentUserExpenseById(id);
		return ResponseEntity.noContent().build();
	}
}
