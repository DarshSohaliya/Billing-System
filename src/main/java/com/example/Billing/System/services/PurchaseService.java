package com.example.Billing.System.services;

import com.example.Billing.System.DTO.BillDTO;
import com.example.Billing.System.DTO.ProductItemDTO;
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


    @Autowired
    WhatsappService whatsappService;

    public BillDTO purchase(PurchaseDTO purchaseDTO) {

        Customer customer = new Customer();
        customer.setName(purchaseDTO.getCustomerName());
        customer.setMobile(purchaseDTO.getMobileNumber());
        customer  = customerRepository.save(customer);

        Product product = productRepository.findByname(purchaseDTO.getProductName());

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
        bill.setAmount(total);
        bill.setGst(gst);
        bill.setCustomer(customer);
        bill.setPaymentStatus("UNPAID");
        bill = billRepository.save(bill);

        BillItem billItem  = new BillItem();
        billItem.setBill(bill);
        billItem.setProductName(product.getName());
        billItem.setQuantity(quantity);
        billItem.setSubtotal(subtotal);
        billItem = billItemRepository.save(billItem);

        bill.setBillItemList(Collections.singletonList(billItem));

        product.setStockCount(product.getStockCount() - quantity);
        productRepository.save(product);

        ProductItemDTO productItemDTO =new ProductItemDTO();
        productItemDTO.setProductName(purchaseDTO.getProductName());
        productItemDTO.setTotalPrice(billItem.getSubtotal());
        productItemDTO.setQuantity(purchaseDTO.getQuantity());

        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerName(bill.getCustomer().getName());
        billDTO.setPurchaseDate(bill.getDate());
        billDTO.setBillId(bill.getBillId());
        billDTO.setGstAmount(bill.getGst());
        billDTO.setSubTotal(bill.getAmount());
        billDTO.setPaymentStatus(bill.getPaymentStatus());
        billDTO.setTotalAmount(billItem.getSubtotal());
        billDTO.setCustomerMobile(bill.getCustomer().getMobile());
        billDTO.setItems(Collections.singletonList(productItemDTO));

        if (product.getStockCount() < product.getMinStockThreshold()) {
            String adminNumber = "+916351029290"; // replace with real admin number
            String alertMessage = "⚠️ Low stock alert!\nProduct: " + product.getName() +
                    "\nRemaining: " + product.getStockCount();
            whatsappService.sendMessage(adminNumber, alertMessage);
        }



        return  billDTO;
    }
}
