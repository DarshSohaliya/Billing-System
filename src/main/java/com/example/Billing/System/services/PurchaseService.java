package com.example.Billing.System.services;

import com.example.Billing.System.DTO.PurchaseDTO;
import com.example.Billing.System.Repositorys.BillItemRepository;
import com.example.Billing.System.Repositorys.BillRepository;
import com.example.Billing.System.Repositorys.CustomerRepository;
import com.example.Billing.System.Repositorys.ProductRepository;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.models.BillItem;
import com.example.Billing.System.models.Customer;
import com.example.Billing.System.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;

@Service
public class PurchaseService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BillRepository billRepository;

    @Autowired
    BillItemRepository  billItemRepository;

    @Autowired
    ProductRepository productRepository;


    public Bill purchase(PurchaseDTO purchaseDTO) {

        Customer customer = new Customer();
        customer.setName(purchaseDTO.getCustomerName());
        customer.setMobile(purchaseDTO.getMobileNumber());
        customer  = customerRepository.save(customer);

        Product product = productRepository.findByProdId(purchaseDTO.getProdId());

        if(purchaseDTO.getQuantity() > product.getStockCount()){
            throw  new RuntimeException("Stock is not available");
        }

        double price = product.getPrice();
        int quantity = purchaseDTO.getQuantity();
        double subtotal = price * quantity;
        double gst = subtotal * 0.18;
        double total = subtotal + gst;


        Bill bill = new Bill();
        bill.setDate(Date.valueOf(LocalDate.now()));
        bill.setAmout(total);
        bill.setGst(gst);
        bill.setCustomer(customer);
        bill = billRepository.save(bill);

        BillItem billItem  = new BillItem();
        billItem.setBill(bill);
        billItem.setProduct(product);
        billItem.setQuantity(quantity);
        billItem.setSubtotal(subtotal);
        billItem = billItemRepository.save(billItem);

        bill.setBillItemList(Collections.singletonList(billItem));

        product.setStockCount(product.getStockCount() - quantity);
        productRepository.save(product);

        return  bill;
    }
}
