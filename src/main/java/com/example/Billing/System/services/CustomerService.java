package com.example.Billing.System.services;

import com.example.Billing.System.Repositorys.CustomerRepository;
import com.example.Billing.System.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service




public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity<?> CreateCustomer(Customer customer){
        try {
            customerRepository.save(customer);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Not Created");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Customer Created SuccessFully");

    }
}
