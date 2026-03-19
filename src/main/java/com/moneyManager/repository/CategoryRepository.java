package com.moneyManager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moneyManager.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
	
	List<CategoryEntity> findByProfile_Id(Long profileId);

	Optional<CategoryEntity> findByIdAndProfile_Id(Long id, Long profileId);
	
	List<CategoryEntity> findByTypeAndProfile_Id(String type, Long profileId);

	Boolean existsByNameAndProfile_Id(String name, Long profileId);
}
