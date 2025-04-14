package com.example.Billing.System.Repositorys;

import com.example.Billing.System.models.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem,Long> {

}
