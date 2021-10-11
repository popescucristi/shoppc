package com.PComponents.repository;

import com.PComponents.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findAllByProductName(String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE %?1%")
    public List<Product> search(String keyword);
}