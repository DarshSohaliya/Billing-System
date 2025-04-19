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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Autowired
    PaymentService paymentService;

    public ResponseEntity<?> purchase(PurchaseDTO purchaseDTO) throws Exception {


        System.out.println(purchaseDTO.getPaymentId());

       boolean paymentSuccess = paymentService.verifyPayment(String.valueOf(purchaseDTO.getPaymentId()));
        Customer customer = customerRepository.findBycustomerId(purchaseDTO.getCustomerId());


        String userPhone = purchaseDTO.getMobileNumber();
       String name = purchaseDTO.getCustomerName();

   double subtotal =0;
      System.out.println(paymentSuccess);

        if (!paymentSuccess){

            String failMessage = "❌ Hello " + name + ", your payment failed. Please try again.";
            whatsappService.sendWhatsApp(userPhone, failMessage);
           whatsappService.sendSMS(userPhone, failMessage);

            throw new RuntimeException("Payment Failed !!");
        }


//        Customer customer = new Customer();
//        customer.setName(purchaseDTO.getCustomerName());
//        customer.setMobile(purchaseDTO.getMobileNumber());
//        customer  = customerRepository.save(customer);


        Bill bill = new Bill();
        bill.setDate(Date.valueOf(LocalDate.now()));
        bill.setCustomer(customer);
        bill.setPaymentStatus("PAID");
        bill = billRepository.save(bill);
        List<BillItem> billItems = new ArrayList<>();
        List<ProductItemDTO> productItemDTOList = new ArrayList<>();
        System.out.println("Items received: " + purchaseDTO.getItems());

        for(ProductItemDTO item:purchaseDTO.getItems()){
            String prodname = item.getProductName();
            System.out.println(prodname);

            Product product = productRepository.findByname(prodname);


            if (product == null) {
                throw new RuntimeException("Product not found: " + item.getProductName());
            }

            if(item.getQuantity() > product.getStockCount()){
                throw new RuntimeException("Stock not available for: " + product.getName());
            }

            double itemSubtotal = product.getPrice() * item.getQuantity();
            subtotal += itemSubtotal;

            BillItem billItem  = new BillItem();
            billItem.setProductName(product.getName());
            billItem.setQuantity(item.getQuantity());
            billItem.setSubtotal(itemSubtotal);
            billItem = billItemRepository.save(billItem);
            billItems.add(billItem);


            product.setStockCount(product.getStockCount() - item.getQuantity());
            productRepository.save(product);

            ProductItemDTO productItemDTO =new ProductItemDTO();
            productItemDTO.setProductName(product.getName());
            productItemDTO.setTotalPrice(itemSubtotal);
            productItemDTO.setQuantity(item.getQuantity());
            productItemDTOList.add(productItemDTO);

            if (product.getStockCount() < product.getMinStockThreshold()) {
                String adminNumber = "+916351029290"; // replace with real admin number
                String alertMessage = "⚠️ Low stock alert!\nProduct: " + product.getName() +
                        "\nRemaining: " + product.getStockCount();
                whatsappService.sendSMS(adminNumber, alertMessage);
                whatsappService.sendWhatsApp(adminNumber,alertMessage);
            }

        }

//        double price = product.getPrice();
//        int quantity = purchaseDTO.getQuantity();
//        double subtotal = 0;
        double gst = subtotal * 0.18;
        double total = subtotal + gst;


        bill.setAmount(total);
        bill.setGst(gst);
        bill.setBillItemList(billItems);
        billRepository.save(bill);


            String message = "✅ Hello " + name + ", your payment of ₹" + total + " was successful. Thank you for your purchase!";
            whatsappService.sendSMS(userPhone,message);
            whatsappService.sendWhatsApp(userPhone,message);



        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerName(customer.getName());
        billDTO.setCustomerMobile(customer.getMobile());
        billDTO.setPurchaseDate(bill.getDate());
        billDTO.setBillId(bill.getBillId());
        billDTO.setGstAmount(gst);
        billDTO.setSubTotal(subtotal);
        billDTO.setTotalAmount(total);
        billDTO.setPaymentStatus(bill.getPaymentStatus());
        billDTO.setItems(productItemDTOList);





        return ResponseEntity.ok(billDTO);
    }
}
