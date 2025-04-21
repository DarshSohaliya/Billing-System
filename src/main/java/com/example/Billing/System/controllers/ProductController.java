package com.example.Billing.System.controllers;

import com.example.Billing.System.DTO.ResponseDTO;
import com.example.Billing.System.models.Product;
import com.example.Billing.System.services.ProductService;
import com.sun.jdi.event.ExceptionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> AddProduct(@RequestBody Product product){
        try {
          Product savedproduct =  productService.AddProduct(product);

              return ResponseEntity.ok(new ResponseDTO<>("Products",savedproduct));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product");
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> GetAll(){
        try {
            List<Product> products = (List<Product>) productService.GetAll();

            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>("No Products Found",null));
            }else{
                return ResponseEntity.ok(new ResponseDTO<>("Products",products));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while fetching products.");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> GetById(@PathVariable(name = "id") Long id){
       try {
            Product product = productService.GetById(id);

            return ResponseEntity.ok(new ResponseDTO<>("Product Fetched SuccessFulyy",product));
       }
       catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body("Product Not Found");
       }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> Update(@PathVariable(name = "id") Long id,@RequestBody Product product){
        try {
           Product Currentproduct= productService.Update(id,product);

            return ResponseEntity.ok(new ResponseDTO<>("Product Was Updated",Currentproduct));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update Was not SuccessFull");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> Delete(@PathVariable(name = "id") Long id){
       return productService.Delete(id);
    }
}
