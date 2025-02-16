package com.rest.springbootkata.repository;

import com.rest.springbootkata.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInventoryRepository extends JpaRepository<Product, Integer> {

}
