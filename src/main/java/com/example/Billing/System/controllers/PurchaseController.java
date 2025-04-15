package com.example.Billing.System.controllers;

import com.example.Billing.System.DTO.BillDTO;
import com.example.Billing.System.DTO.PurchaseDTO;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.services.PaymentService;
import com.example.Billing.System.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    PaymentService paymentService;

    @PostMapping("/purchase")
    public BillDTO purchaseproduct(@RequestBody  PurchaseDTO purchaseDTO){
        return purchaseService.purchase(purchaseDTO);

    }

    @PostMapping("/pay")
    public String payment(@RequestParam long customerId, double amountPaid){
      return  paymentService.processPayment(customerId,amountPaid);

    }
}
