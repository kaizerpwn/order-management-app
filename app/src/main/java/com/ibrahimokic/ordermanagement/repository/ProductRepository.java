package com.ibrahimokic.ordermanagement.repository;

import com.ibrahimokic.ordermanagement.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
