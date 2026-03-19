package com.moneyManager.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Profiles_table")
public class ProfileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fullName;
	
	@Column(unique=true)
	private String email;
	
	private String password;
	
	private String profileImgUrl;
	
	@Column(updatable=false,insertable=true)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(updatable=true,insertable=false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}
