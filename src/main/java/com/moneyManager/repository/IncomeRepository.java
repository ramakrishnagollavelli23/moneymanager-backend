package com.moneyManager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moneyManager.entity.IncomeEntity;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {
	
	List<IncomeEntity> findByProfile_IdOrderByDateDesc(Long profileId);
	
	List<IncomeEntity> findTop5ByProfile_IdOrderByDateDesc(Long profileId);
	
	@Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.profile.id = :profileId")
	BigDecimal findTotalIncomeByProfile_Id(@Param("profileId") Long profileId);

	List<IncomeEntity> findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(Long profileId,LocalDate startDate,LocalDate endDate, String name, Sort sort);

	List<IncomeEntity> findByProfile_IdAndDateBetween(Long profileId,LocalDate startDate, LocalDate endDate);
	
}