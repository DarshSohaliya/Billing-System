package com.example.Billing.System.Repositorys;

import com.example.Billing.System.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByProdId(long prodId);

    void  deleteByProdId(long prodId);

}
