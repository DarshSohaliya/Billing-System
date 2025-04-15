package com.example.Billing.System.services;

import com.example.Billing.System.Repositorys.BillRepository;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.models.BillItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
public class DailyReportSender {
    @Autowired
    BillRepository billRepository;

    @Autowired
    EmailService emailService;

    @Scheduled(fixedRate = 1000)


    @Transactional
    public void sendDailyReport(){
       LocalDate reportDate =  LocalDate.now();

        List<Bill> bills = billRepository.findByDate(reportDate);

        StringBuilder report = new StringBuilder("Billing Report for " + reportDate + "\n\n");
        report.append("Customer,Product,Quantity,Subtotal,Total Amount,GST,Amount Paid,Payment Status\n");
        for (Bill bill : bills) {

           String CustomerName = bill.getCustomer().getName();


            for(BillItem item : bill.getBillItemList()) {
                   report.append("Customer :").append(CustomerName).append("\n")
                           .append("Product :").append(item.getProductName()).append("\n")
                           .append("Quantity :").append(item.getQuantity()).append("\n")
                           .append("SubTotal :").append(item.getSubtotal()).append("\n")
                           .append("Amount :").append(item.getBill().getAmount()).append("\n")
                           .append("Product :").append(item.getBill().getGst()).append("\n")
                           .append("Product :").append(item.getBill().getAmountPaid()).append("\n")
                           .append("Product :").append(item.getBill().getPaymentStatus()).append("\n");


            }

        }
        byte[] csvByte = report.toString().getBytes(StandardCharsets.UTF_8);
        InputStreamSource attachment = new ByteArrayResource(csvByte);
        
        emailService.sendMailWithReport("darshsohaliya@gmail.com",
                "Daily Billin Report - " + reportDate ,
                "Please find the attached billing report for " + reportDate,
                attachment,
                "BillingReport-" + reportDate + ".csv");
    }
}
