package com.rest.springbootkata.service;

import com.rest.springbootkata.entity.Product;
import com.rest.springbootkata.repository.JpaInventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private JpaInventoryRepository jpaInventoryRepository;

    public InventoryService(JpaInventoryRepository jpaInventoryRepository) {
        this.jpaInventoryRepository = jpaInventoryRepository;
    }

    public Product addProduct(Product product) {
        return jpaInventoryRepository.save(product);
    }

    public void deleteProduct(Integer productId) {
        jpaInventoryRepository.deleteById(productId);
    }

    public List<Product> getAllProducts() {
        return jpaInventoryRepository.findAll();
    }

    public Product getProductById(Integer productId) {
        return jpaInventoryRepository.findById(productId).orElse(null);
    }

    public List<Product> getLowStockProducts(int lowerLimit) {
        return jpaInventoryRepository.findAll().stream()
                .filter(product -> product.getQuantity() < lowerLimit).collect(Collectors.toList());
    }

    public Product adjustStock(Integer productId, int incrementMount) {
        Product product = getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }
        product.setQuantity(product.getQuantity() + incrementMount);
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Adjustment would result in negative stock.");
        }
        return jpaInventoryRepository.save(product);
    }
}
