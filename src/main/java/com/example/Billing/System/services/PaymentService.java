package com.example.Billing.System.services;

import com.example.Billing.System.DTO.PaymentResponseDTO;
import com.example.Billing.System.DTO.ProductItemDTO;
import com.example.Billing.System.Repositorys.BillRepository;
import com.example.Billing.System.Repositorys.CustomerRepository;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.models.Customer;
import com.razorpay.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class PaymentService {


    private RazorpayClient razorpay;


    public PaymentService(@Value("${razorpay.key_id}") String keyId, @Value("${razorpay.key_secret}") String KeySecret) throws Exception {
        System.out.println("RAZRPAY KEYY ::" + keyId);
        System.out.println("RAZRPAY SECRET ::" + KeySecret);
        if (keyId == null || KeySecret == null) {
            throw new IllegalArgumentException("Razorpay key and secret must be provided.");
        }
        this.razorpay = new RazorpayClient(keyId, KeySecret);
    }

    public PaymentResponseDTO createPayment(double amount, Long CustomerId, String customerName, String mobileNumber, List<ProductItemDTO> items) throws Exception {
        try {
            JSONObject request = new JSONObject();
            request.put("amount", (int) (amount * 100));
            request.put("currency", "INR");
            request.put("description", "Product Purchase");
//


            JSONObject notes = new JSONObject();
            notes.put("customerId", CustomerId);
            notes.put("customerName", customerName);
            notes.put("mobileNumber", mobileNumber);


            JSONArray itemsArray = new JSONArray();
            for (ProductItemDTO item : items) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("productName", (item.getProductName()));
                itemObj.put("quantity", item.getQuantity());
                itemsArray.put(itemObj);
            }
            notes.put("items", itemsArray);
            request.put("notes", notes);

            request.put("notify", new JSONObject().put("sms", true));
            request.put("reminder_enable", true);


            PaymentLink paymentLink = razorpay.paymentLink.create(request);

            String paymentUrl = paymentLink.get("short_url").toString();
            String razorpayLinkId = paymentLink.get("id").toString();

            PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
            paymentResponseDTO.setPaymentLink(paymentUrl);
            paymentResponseDTO.setPaymentLinkId(razorpayLinkId);
            paymentResponseDTO.setAmount(amount);
            return paymentResponseDTO;
        } catch (RazorpayException e) {
            System.out.println("Razorpay Error: " + e.getMessage());
            throw new Exception("Error creating Razorpay payment link: " + e.getMessage());
        }

    }

    public boolean verifyPayment(String paymentLinkId) {
        try {
            PaymentLink paymentLink = razorpay.paymentLink.fetch(paymentLinkId);
            String status = paymentLink.get("status");

            if ("paid".equals(status)) {
                return true;
            } else {
                System.out.println("Payment link not paid. Status: " + status);
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error  payment link: " + e.getMessage());
            return false;
        }
}
//    @Autowired
//    BillRepository billRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    WhatsappService whatsappService;
//
//    public String processPayment(long customerId,double amountPaid) {
//
//    Customer  customer = customerRepository.findBycustomerId(customerId);
//
//        Bill bill = billRepository.findTopByCustomerOrderByCreatedAtDesc(customer);
//
//        if (bill == null){
//            return "No Bill Found for customer.";
//        }
//
//        boolean paymentSuccessful = Double.compare(bill.getAmount(),amountPaid) == 0;
//
//        bill.setPaymentStatus(paymentSuccessful ? "PAID" : "UNPAID");
//        bill.setDate(Date.valueOf(LocalDate.now()));
//        bill.setAmountPaid(amountPaid);
//        billRepository.save(bill);
//
//        String message;
//        if (paymentSuccessful) {
//            message = "✅ Hello " + customer.getName() + ", your payment of ₹" + amountPaid + " was successful. Thank you!";
//        } else {
//            message = "❌ Hello " + customer.getName() + ", payment failed. You were billed ₹" +
//                    bill.getAmount() + " but paid ₹" + amountPaid + ".";
//        }
//
//        whatsappService.sendMessage(customer.getMobile(), message);
//
//        return bill.getPaymentStatus();
    }



