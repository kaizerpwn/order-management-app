package com.ibrahimokic.ordermanagement.repositories;

import com.ibrahimokic.ordermanagement.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
