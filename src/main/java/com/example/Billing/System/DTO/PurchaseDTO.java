package com.example.Billing.System.DTO;

import java.util.List;

public class PurchaseDTO {

    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    private Long  customerId;

//    public Customer getCustomer() {
//        return customer;
//    }

//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }

    private String paymentId;
    private List<ProductItemDTO> items;

    public List<ProductItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ProductItemDTO> items) {
        this.items = items;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

//    public String getCustomerName() {
//        return customerName;
//    }
//
//    public void setCustomerName(String customerName) {
//        this.customerName = customerName;
//    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private String mobileNumber;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    private String productName;

    private int quantity;


}
