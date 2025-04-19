package com.example.Billing.System.controllers;

import com.example.Billing.System.models.Customer;
import com.example.Billing.System.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

   @PostMapping("/create")
    public void CreateUser(Customer customer){
       customerService.CreateCustomer(customer);
   }
}
