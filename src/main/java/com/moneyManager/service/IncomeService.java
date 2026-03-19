package com.moneyManager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.moneyManager.dto.IncomeDto;
import com.moneyManager.entity.CategoryEntity;
import com.moneyManager.entity.IncomeEntity;
import com.moneyManager.entity.ProfileEntity;
import com.moneyManager.repository.CategoryRepository;
import com.moneyManager.repository.IncomeRepository;

@Service
@RequiredArgsConstructor
public class IncomeService {

	private final CategoryRepository categoryRepository;
	private final IncomeRepository incomeRepository;
	private final ProfileService profileService;
	
	public IncomeDto toSaveIncome(IncomeDto incomeDto) {
		
		ProfileEntity profile = profileService.getCurrentProfile();
		CategoryEntity category = categoryRepository.findById(incomeDto.getCategoryId()).orElseThrow(()-> new RuntimeException("Category Not Found"));
		IncomeEntity newIncome = toEntity(incomeDto, profile, category);
		newIncome = incomeRepository.save(newIncome);
		return toDto(newIncome);
		
	}
	
	public List<IncomeDto> getFilterData(LocalDate startDate, LocalDate endDate, String keyword,Sort sort){
		ProfileEntity profile = profileService.getCurrentProfile();
		List<IncomeEntity> incomeList = incomeRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
		return incomeList.stream().map(this::toDto).toList();
	}
	
	public List<IncomeDto> getCurrentUserLatestIncomes(){
		ProfileEntity profile = profileService.getCurrentProfile();
		List<IncomeEntity> list = incomeRepository.findTop5ByProfile_IdOrderByDateDesc(profile.getId());
		return list.stream().map(this::toDto).toList();
	}
	
	public BigDecimal getTotalIncomeForCurrentUser() {
		ProfileEntity profile = profileService.getCurrentProfile();
		BigDecimal total = incomeRepository.findTotalIncomeByProfile_Id(profile.getId());
		return total != null ?total : BigDecimal.ZERO;
	}
	
	public List<IncomeDto> getCurrentMonthIncomesForCurrentUser() {
		ProfileEntity profile = profileService.getCurrentProfile();
		List<IncomeEntity> incomeList = incomeRepository.findByProfile_IdAndDateBetween(profile.getId(), LocalDate.now().withDayOfMonth(1),LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
		return incomeList.stream().map(this::toDto).toList();
	}

	private IncomeEntity toEntity(IncomeDto dto,ProfileEntity profileEntity, CategoryEntity categoryEntity) {
		return IncomeEntity.builder()
				.name(dto.getName())
				.amount(dto.getAmount())
				.icon(dto.getIcon())
				.date(dto.getDate())
				.profile(profileEntity)
				.category(categoryEntity)
				.build();
	}
	
	private IncomeDto toDto(IncomeEntity entity) {
		return IncomeDto.builder()
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
	
	public void deleteCurrentUserIncomeById(Long id) {
		IncomeEntity entity = incomeRepository.findById(id).orElseThrow(()->new RuntimeException("Income Not Found"));
		ProfileEntity profile = profileService.getCurrentProfile();
		if(!entity.getProfile().getId().equals(profile.getId())) {
			throw new RuntimeException("Unauthorized to delete this Income");
		}
		incomeRepository.delete(entity);
	}
	
}
