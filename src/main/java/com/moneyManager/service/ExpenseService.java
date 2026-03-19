package com.moneyManager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.moneyManager.dto.ExpenseDto;
import com.moneyManager.entity.CategoryEntity;
import com.moneyManager.entity.ExpenseEntity;
import com.moneyManager.entity.ProfileEntity;
import com.moneyManager.repository.CategoryRepository;
import com.moneyManager.repository.ExpenseRepository;

@Service
@RequiredArgsConstructor
public class ExpenseService {

	private final CategoryRepository categoryRepository;
	private final ExpenseRepository expenseRepository;
	private final ProfileService profileService;
	
	public ExpenseDto toSaveExpense(ExpenseDto expenseDto) {
		
		ProfileEntity profile = profileService.getCurrentProfile();
		CategoryEntity category = categoryRepository.findById(expenseDto.getCategoryId()).orElseThrow(()-> new RuntimeException("Category Not Found"));
		ExpenseEntity newExpense = toEntity(expenseDto, profile, category);
		newExpense = expenseRepository.save(newExpense);
		return toDto(newExpense);
		
	}
	
	public List<ExpenseDto> getFilterData(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
		ProfileEntity profile = profileService.getCurrentProfile();
		List<ExpenseEntity> expenseList = expenseRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
		return expenseList.stream().map(this::toDto).toList();
	}
	
	public List<ExpenseDto> getCurrentMonthExpenseForCurrentUser(){
		ProfileEntity profile = profileService.getCurrentProfile();
		List<ExpenseEntity> expenseList = expenseRepository.findByProfile_IdAndDateBetween(profile.getId(),LocalDate.now().withDayOfMonth(1),LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
		return expenseList.stream().map(this::toDto).toList();
	}

	public List<ExpenseDto> getCurrentUserLatestExpenses(){
		ProfileEntity profile = profileService.getCurrentProfile();
		List<ExpenseEntity> list = expenseRepository.findTop5ByProfile_IdOrderByDateDesc(profile.getId());
		return list.stream().map(this::toDto).toList();
	}
	
	public BigDecimal getTotalExpenseForCurrentUser() {
		ProfileEntity profile = profileService.getCurrentProfile();
		BigDecimal total = expenseRepository.findTotalExpenseByProfile_Id(profile.getId());
		return total != null ?total : BigDecimal.ZERO;
	}
	
	private ExpenseEntity toEntity(ExpenseDto dto,ProfileEntity profileEntity, CategoryEntity categoryEntity) {
		return ExpenseEntity.builder()
				.name(dto.getName())
				.amount(dto.getAmount())
				.icon(dto.getIcon())
				.date(dto.getDate())
				.profile(profileEntity)
				.category(categoryEntity)
				.build();
	}
	
	public List<ExpenseDto> getExpensesForUserOnDate(Long profileId, LocalDate date){
		List<ExpenseEntity> list = expenseRepository.findByProfile_IdAndDate(profileId, date);
		return list.stream().map(this::toDto).toList();
	}
	
	private ExpenseDto toDto(ExpenseEntity entity) {
		return ExpenseDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.amount(entity.getAmount())
				.categoryId(entity.getCategory() != null? entity.getCategory().getId():null)
				.categoryName(entity.getCategory() != null? entity.getCategory().getName():"N/A")
				.icon(entity.getIcon())
				.date(entity.getDate())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();				
	}

	public void deleteCurrentUserExpenseById(Long id) {
		ExpenseEntity entity = expenseRepository.findById(id).orElseThrow(()->new RuntimeException("Expense Not Found"));
		ProfileEntity profile = profileService.getCurrentProfile();
		if(!entity.getProfile().getId().equals(profile.getId())) {
			throw new RuntimeException("Unauthorized to delete this Expense");
		}
		expenseRepository.delete(entity);
	}
}
