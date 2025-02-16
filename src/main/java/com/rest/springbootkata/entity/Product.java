package com.rest.springbootkata.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer quantity;

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return this.name;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setId(Integer productId) {
        this.id = productId;
    }

    public Integer getId() {
        return this.id;
    }
}