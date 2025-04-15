package com.example.Billing.System.services;

import com.example.Billing.System.Repositorys.BillRepository;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.models.BillItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyReportSender {
    @Autowired
    BillRepository billRepository;

    @Autowired
    EmailService emailService;

    @Scheduled(cron = "0 0 21 * * *")
    @Transactional
    public void sendDailyReport(){
       LocalDate reportDate =  LocalDate.now();

        List<Bill> bills = billRepository.findByDate(reportDate);

        StringBuilder report = new StringBuilder("Billing Report for " + reportDate + "\n\n");

        for (Bill bill : bills) {
            report.append("Customer :").append(bill.getCustomer()).append("\n");

            for(BillItem item : bill.getBillItemList()) {
                    report.append("Product :").append(item.getProductName())
                        .append("Quantity :").append(item.getQuantity())
                        .append("Total :").append(item.getSubtotal())
                        .append("\n");
            }

            report.append("Total Amount :").append(bill.getAmout()).append("\n")
                    .append("GST :").append(bill.getGst()).append("\n")
                    .append("Amount Paid").append(bill.getAmountPaid()).append("\n")
                    .append("Payment Status:").append(bill.getPaymentStatus()).append("\n");
        }
        
        emailService.sendMailWithReport("gopalsohaliya@gmail.com","Daily Billin Report - " + reportDate , report.toString());
    }
}
