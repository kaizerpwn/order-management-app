package com.ibrahimokic.ordermanagement.repository;

import com.ibrahimokic.ordermanagement.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE :currentDate BETWEEN p.availableFrom AND p.availableUntil") 
    List<Product> findAvailableProducts(LocalDate currentDate);
}
