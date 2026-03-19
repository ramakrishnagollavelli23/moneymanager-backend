package com.moneyManager.controller;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyManager.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
	
	private final DashboardService dashboardService;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getDashboardData(){
		Map<String, Object> dashboardData = dashboardService.getDashBoardDetails();
		return ResponseEntity.status(HttpStatus.OK).body(dashboardData);
	}
}
