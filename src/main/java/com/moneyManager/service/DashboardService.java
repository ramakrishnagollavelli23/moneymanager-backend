package com.moneyManager.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moneyManager.dto.ExpenseDto;
import com.moneyManager.dto.IncomeDto;
import com.moneyManager.dto.RecentTransactionDto;
import com.moneyManager.entity.ProfileEntity;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final ProfileService profileService;
	private final ExpenseService expenseService;
	private final IncomeService incomeService;
	
	public Map<String, Object> getDashBoardDetails(){
		ProfileEntity profile = profileService.getCurrentProfile();
		Map<String, Object> returnValue = new LinkedHashMap<>();
		List<ExpenseDto> expenseList = expenseService.getCurrentUserLatestExpenses();
		List<IncomeDto> incomeList = incomeService.getCurrentUserLatestIncomes();

		List<RecentTransactionDto> recentList =  Stream.concat(expenseList.stream().map(expense -> RecentTransactionDto.builder()
				.id(expense.getId())
				.name(expense.getName())
				.createdAt(expense.getCreatedAt())
				.updatedAt(expense.getUpdatedAt())
				.icon(expense.getIcon())
				.amount(expense.getAmount())
				.date(expense.getDate())
				.profileId(profile.getId())
				.type("expense")
				.build()
		),incomeList.stream().map(income->RecentTransactionDto.builder()
				.id(income.getId())
				.name(income.getName())
				.createdAt(income.getCreatedAt())
				.updatedAt(income.getUpdatedAt())
				.icon(income.getIcon())
				.amount(income.getAmount())
				.date(income.getDate())
				.profileId(profile.getId())
				.type("income")
				.build()
				)).sorted((a,b)->{
					int cmp = a.getDate().compareTo(b.getDate());
					if(cmp==0 && a.getCreatedAt() != null && b.getCreatedAt() != null){
						return b.getCreatedAt().compareTo(a.getCreatedAt());
					}
					return cmp;
				}).collect(Collectors.toList());
		BigDecimal totalBalance =incomeService.getTotalIncomeForCurrentUser().subtract(expenseService.getTotalExpenseForCurrentUser());
		returnValue.put("totalBalance", totalBalance);
		
		returnValue.put("totalIncome", incomeService.getTotalIncomeForCurrentUser());
		returnValue.put("totalExpense", expenseService.getTotalExpenseForCurrentUser());
		returnValue.put("recent5Expenses", expenseList);
		returnValue.put("recent5Incomes", incomeList);
		returnValue.put("recentTranscations", recentList);
		
		return returnValue;
		
	}
}
