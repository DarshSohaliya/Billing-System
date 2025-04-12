package com.example.Billing.System.Repositorys;

import com.example.Billing.System.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByProdId(long prodId);

//    @Query("DELETE FROM Product p WHERE p.prodId = :prodId")
//    void  deleteByProdId(@Param("prodId") long prodId);

}
