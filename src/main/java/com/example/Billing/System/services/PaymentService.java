package com.example.Billing.System.services;

import com.example.Billing.System.Repositorys.BillRepository;
import com.example.Billing.System.Repositorys.CustomerRepository;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

@Service
public class PaymentService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    WhatsappService whatsappService;

    public String processPayment(long customerId,double amountPaid) {

    Customer  customer = customerRepository.findBycustomerId(customerId);

        Bill bill = billRepository.findTopByCustomerOrderByCreatedAtDesc(customer);

        if (bill == null){
            return "No Bill Found for customer.";
        }

        boolean paymentSuccessful = Double.compare(bill.getAmount(),amountPaid) == 0;

        bill.setPaymentStatus(paymentSuccessful ? "PAID" : "UNPAID");
        bill.setDate(Date.valueOf(LocalDate.now()));
        bill.setAmountPaid(amountPaid);
        billRepository.save(bill);

        String message;
        if (paymentSuccessful) {
            message = "✅ Hello " + customer.getName() + ", your payment of ₹" + amountPaid + " was successful. Thank you!";
        } else {
            message = "❌ Hello " + customer.getName() + ", payment failed. You were billed ₹" +
                    bill.getAmount() + " but paid ₹" + amountPaid + ".";
        }

        whatsappService.sendMessage(customer.getMobile(), message);

        return bill.getPaymentStatus();
    }


}
