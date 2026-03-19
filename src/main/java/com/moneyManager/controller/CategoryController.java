package com.moneyManager.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyManager.dto.CategoryDto;
import com.moneyManager.service.CategoryService;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
	
	private final CategoryService categoryService;
	
	@PostMapping()
	public ResponseEntity<CategoryDto> saveCategoty(@RequestBody CategoryDto categoryDto){
		CategoryDto savedCategory = categoryService.saveCategory(categoryDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
	}
	
	@GetMapping()
	public ResponseEntity<List<CategoryDto>> getCategories(){
		List<CategoryDto> categories = categoryService.getCategoriesForCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(categories);
	}
	
	@GetMapping("/{type}")
	public ResponseEntity<List<CategoryDto>> getCategoriesByType(@PathVariable String type){
		List<CategoryDto> categories = categoryService.getCategoriesByType(type);
		return ResponseEntity.status(HttpStatus.OK).body(categories);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto ){
		CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
		return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
	}
}
