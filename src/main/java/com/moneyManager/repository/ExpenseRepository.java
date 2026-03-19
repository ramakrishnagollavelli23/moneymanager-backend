package com.moneyManager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moneyManager.entity.ExpenseEntity;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>{
	
	List<ExpenseEntity> findByProfile_IdOrderByDateDesc(Long profileId);
	
	List<ExpenseEntity> findTop5ByProfile_IdOrderByDateDesc(Long profileId);
	
	@Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
	BigDecimal findTotalExpenseByProfile_Id(@Param("profileId") Long profileId);

	List<ExpenseEntity> findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(Long profileId,LocalDate startDate,LocalDate endDate, String name, Sort sort);

	List<ExpenseEntity> findByProfile_IdAndDateBetween(Long profileId,LocalDate startDate, LocalDate endDate);
	
	List<ExpenseEntity> findByProfile_IdAndDate(Long profileId, LocalDate date);
	
}