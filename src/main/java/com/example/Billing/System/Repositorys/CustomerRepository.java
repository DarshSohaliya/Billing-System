package com.example.Billing.System.Repositorys;

import com.example.Billing.System.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Customer findBycustomerId(long id);
}
