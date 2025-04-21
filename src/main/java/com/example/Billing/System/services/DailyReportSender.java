package com.example.Billing.System.services;

import com.example.Billing.System.Repositorys.BillRepository;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.models.BillItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class DailyReportSender {
    @Autowired
    BillRepository billRepository;

    @Autowired
    EmailService emailService;

    @Value("${admin.email}")
   private String adminemail;
    //@Scheduled(fixedRate = 1000)


    @Transactional
    public void sendDailyReport(){
       Date reportDate = Date.valueOf(LocalDate.now());
      System.out.println(reportDate);
        List<Bill> bills = billRepository.findByDate(reportDate);
      System.out.println(bills);
        StringBuilder report = new StringBuilder("Billing Report for " + reportDate + "\n\n");
        //report.append("Customer,Product,Quantity,Subtotal,Total Amount,GST,Amount Paid,Payment Status\n");
        for (Bill bill : bills) {

           String CustomerName = bill.getCustomer().getName();

          System.out.println(CustomerName);
            for(BillItem item : bill.getBillItemList()) {
                System.out.println("Item::::" + item.getProductName());
                   report.append("Customer :").append(CustomerName).append(",")
                           .append("ProductName :").append(item.getProductName()).append(",")
                           .append("Quantity :").append(item.getQuantity()).append(",")
                           .append("SubTotal :").append(item.getSubtotal()).append(",")
                           .append("Amount :").append(item.getBill().getAmount()).append(",")
                           .append("ProductGst :").append(item.getBill().getGst()).append(",")
                           .append("ProductStatus :").append(item.getBill().getPaymentStatus()).append("\n");
            }
        }
        byte[] csvByte = report.toString().getBytes(StandardCharsets.UTF_8);
        InputStreamSource attachment = new ByteArrayResource(csvByte);
        
        emailService.sendMailWithReport(adminemail,
                "Daily Billin Report - " + reportDate ,
                "billing report for " + reportDate,
                attachment,
                "BillingReport-" + reportDate + ".csv");
    }
}
