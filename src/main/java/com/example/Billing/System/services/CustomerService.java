package com.example.Billing.System.services;

import com.example.Billing.System.Repositorys.CustomerRepository;
import com.example.Billing.System.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service




public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public void CreateCustomer(Customer customer){
        customerRepository.save(customer);
    }
}
