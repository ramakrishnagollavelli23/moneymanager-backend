package com.moneyManager.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.moneyManager.dto.CategoryDto;
import com.moneyManager.entity.CategoryEntity;
import com.moneyManager.entity.ProfileEntity;
import com.moneyManager.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
	
	public CategoryDto saveCategory(CategoryDto categoryDto) {
		ProfileEntity profile = profileService.getCurrentProfile();
		
		if(categoryRepository.existsByNameAndProfile_Id(categoryDto.getName(), profile.getId())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with this name is already existed!");
		}
		
		CategoryEntity newCategory = toEntity(categoryDto, profile);
		newCategory = categoryRepository.save(newCategory);
		return toDto(newCategory);
	}
	
	private CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profileEntity) {
		return CategoryEntity.builder()
				.name(categoryDto.getName())	
				.type(categoryDto.getType())
				.icon(categoryDto.getIcon())
				.profile(profileEntity)
				.build();
	}
	
	private CategoryDto toDto(CategoryEntity categoryEntity) {
		return CategoryDto.builder()
				.name(categoryEntity.getName())	
				.id(categoryEntity.getId())
				.profileId(categoryEntity.getProfile() != null? categoryEntity.getProfile().getId():null)
				.type(categoryEntity.getType())
				.icon(categoryEntity.getIcon())
				.createdAt(categoryEntity.getCreatedAt())
				.updatedAt(categoryEntity.getUpdatedAt())
				.build();
	}
	
	public List<CategoryDto> getCategoriesForCurrentUser(){
		ProfileEntity profile = profileService.getCurrentProfile();
		List<CategoryEntity> categories = categoryRepository.findByProfile_Id(profile.getId());
		return categories.stream().map(this::toDto).toList();
	}
	
	public List<CategoryDto> getCategoriesByType(String type){
		ProfileEntity entity = profileService.getCurrentProfile();
		List<CategoryEntity> categoriesType = categoryRepository.findByTypeAndProfile_Id(type,entity.getId());
		return categoriesType.stream().map(this::toDto).toList();
	}
	
	public CategoryDto updateCategory(Long id,CategoryDto categoryDto) {
		
		ProfileEntity profile = profileService.getCurrentProfile();
		
		return categoryRepository.findByIdAndProfile_Id(id, profile.getId())
				.map((currentProfile)->{
					currentProfile.setIcon(categoryDto.getIcon());
					currentProfile.setName(categoryDto.getName());
					currentProfile.setType(categoryDto.getType());
					categoryRepository.save(currentProfile);
					return toDto(currentProfile);
				})
				.orElseThrow(()->new RuntimeException("Category is not found"));
		
	}
	
}