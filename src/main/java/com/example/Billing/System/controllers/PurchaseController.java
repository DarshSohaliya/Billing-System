package com.example.Billing.System.controllers;

import com.example.Billing.System.DTO.PurchaseDTO;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @PostMapping("/purchase")
    public Bill purchaseproduct(@RequestBody  PurchaseDTO purchaseDTO){
        return purchaseService.purchase(purchaseDTO);

    }
}
