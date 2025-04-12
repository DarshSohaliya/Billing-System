package com.example.Billing.System.services;


import com.example.Billing.System.DTO.ResponseDTO;
import com.example.Billing.System.Repositorys.ProductRepository;
import com.example.Billing.System.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;


    public Product AddProduct(Product product) {
        try {
            productRepository.save(product);
        }
        catch (Exception e){
          throw  new RuntimeException("Failed to add product");
        }
        return product;
    }

    public List<Product> GetAll() {
        List<Product>  products;
    try{
       products =  productRepository.findAll();
    }
    catch (Exception e){
        throw new RuntimeException("Product not Found");
    }
     return products;
    }

    public Product GetById(Long id) {

        Product product;
        try {
            product = productRepository.findByProdId(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to Get Product");
        }
        return product;
    }

    public Product Update(Long id,Product product) {
        Product product1;
        try{

           product1 =  productRepository.findByProdId(id);
          if(product1 == null){
              throw new RuntimeException("Product is Not Exist");
          }

          product1.setName(product.getName());
          product1.setPrice(product.getPrice());
          product1.setStockCount(product.getStockCount());
       }
       catch (Exception e){
            throw  new RuntimeException("Product was Not Updated");
       }
        return productRepository.save(product1);
    }

    public void Delete(Long id) {
        if(!productRepository.existsById(id)){
            throw new RuntimeException("Product Not Found");
        }
        try {
            productRepository.deleteById(id);

        }
        catch (Exception e){
            throw  new RuntimeException("Product Was not Deleted");
        }
    }
}
