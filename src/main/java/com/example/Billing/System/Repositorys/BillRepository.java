package com.example.Billing.System.Repositorys;

import com.example.Billing.System.models.Bill;
import com.example.Billing.System.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {

    List<Bill> findByDate(LocalDate reportDate);

    Bill findTopByCustomerOrderByCreatedAtDesc(Customer customer);
}
