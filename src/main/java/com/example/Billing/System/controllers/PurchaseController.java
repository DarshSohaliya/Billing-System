package com.example.Billing.System.controllers;

import com.example.Billing.System.DTO.BillDTO;
import com.example.Billing.System.DTO.PaymentResponseDTO;
import com.example.Billing.System.DTO.ProductItemDTO;
import com.example.Billing.System.DTO.PurchaseDTO;
import com.example.Billing.System.Repositorys.CustomerRepository;
import com.example.Billing.System.Repositorys.ProductRepository;
import com.example.Billing.System.models.Bill;
import com.example.Billing.System.models.Customer;
import com.example.Billing.System.models.Product;
import com.example.Billing.System.services.PaymentService;
import com.example.Billing.System.services.PurchaseService;
import com.razorpay.PaymentLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/purchase/initiate")
    public ResponseEntity<?> purchaseproduct(@RequestBody  PurchaseDTO purchaseDTO){
        Customer customer = customerRepository.findBycustomerId(purchaseDTO.getCustomerId());

        if(customer == null){
            throw  new RuntimeException("Without user Purchase Can not Make");
        }
         double subtotal=0;
        try {

            System.out.println("ITEMS:::"+purchaseDTO.getItems());
            for(ProductItemDTO item : purchaseDTO.getItems()) {
                String prodname = item.getProductName();
                System.out.println("NAME:::"+prodname);
                Product product = productRepository.findByname(prodname);

                if (item.getQuantity() > product.getStockCount()) {
                    throw new RuntimeException("Stock is Unavailable");
                }
                subtotal += item.getQuantity() * product.getPrice();
            }
//            double subtotal = purchaseDTO.getQuantity() * product.getPrice();
            double gst  = subtotal * 0.18;
            double total = subtotal + gst;





//            purchaseDTO.setPaymentId(paymentId);
//            System.out.println(purchaseDTO.getPaymentId());
            PaymentResponseDTO checkoutUrl = paymentService.createPayment(total,purchaseDTO.getCustomerId(),
                    customer.getName(),
                    customer.getMobile(),
                    purchaseDTO.getItems());

             purchaseDTO.setPaymentId(checkoutUrl.getPaymentLinkId());

            return ResponseEntity.ok(Map.of("paymentLink",checkoutUrl,"amount",total));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/purchase/conform")
    public ResponseEntity<?> payment(@RequestBody  PurchaseDTO purchaseDTO) throws Exception {
        System.out.println("Payment ID received: " + purchaseDTO.getPaymentId());
      return  purchaseService.purchase(purchaseDTO);

    }
}
