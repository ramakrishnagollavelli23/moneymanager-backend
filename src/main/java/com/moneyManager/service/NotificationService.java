package com.moneyManager.service;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.moneyManager.dto.ExpenseDto;
import com.moneyManager.entity.ProfileEntity;
import com.moneyManager.repository.ProfileRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

	private final ExpenseService expenseService;
	private final ProfileRepository profileRepository;
	private final EmailService emailService;

	@Value("${money.manager.frontend.url}")
	private String frontendBaseUrl;

	@Scheduled(cron = "0 0 22 * * *",zone = "IST")
	public void sendDailyIncomeExpenseRemainder() {
		List<ProfileEntity> profiles = profileRepository.findAll();
		for (ProfileEntity profile : profiles) {
			String body = "Hi " + profile.getFullName() + ",<br/><br/>"
					+"This is a friendly remainder to add your income and expenses for today in Money Manager.<br/><br/>"
					+"<a href="+frontendBaseUrl+" style=display:inline-block; padding: 10px 20px; background-color: #4CAF50;color:#fff;text-decoration:none; border-radius: 5px; font-weight: bold;'>Go to Money Manager</a>"
					+"<br/><br/>Best Regards, <br/>Money Manager Team";
			emailService.sendEmail(profile.getEmail(), "Daily Remainder : Add Your Incomes and Expanses", body);
		}
	}
	
	@Scheduled(cron = "0 0 23 * * *", zone = "IST")
	public void sendDailyExpensesSummary() {
		List<ProfileEntity> profiles = profileRepository.findAll();
		for(ProfileEntity profile : profiles) {
			List<ExpenseDto> todayExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
			if(!todayExpenses.isEmpty()) {
				StringBuffer table = new StringBuffer();
				table.append("<table style='border-collapse:collapse; width:100%;'>");
				table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;'>S.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;'>Amount</th><th style='border:1px solid #ddd;padding:8px;'>Category</th></tr>");
				int i=0;
				for(ExpenseDto expense : todayExpenses) {
					table.append("<tr>");
					table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(++i).append("</td>");
					table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getName()).append("</td>");
					table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getAmount()).append("</td>");
					table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getCategoryId()!=null?expense.getCategoryName():"N/A").append("</td>");
					table.append("</tr>");
				}
				table.append("</table>");
				String body ="Hi "+ profile.getFullName()+", <br/> <br/> Here is a summary of your expense for today : <br/>"+table+"<br/><br/>Best Regards,<br/>Money Manager Team";
				emailService.sendEmail(profile.getEmail(), "Your Daily Expenses Summary", body);
			}
		}
	}
}