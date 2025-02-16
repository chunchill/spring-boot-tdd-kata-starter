package com.rest.springbootkata.service;

import com.rest.springbootkata.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final MartEmployeeDiscountService martEmployeeDiscountService;

    public OrderService(MartEmployeeDiscountService martEmployeeDiscountService) {
        this.martEmployeeDiscountService = martEmployeeDiscountService;
    }

    public double placeOrder(Employee employee, double orderAmount) {
        return martEmployeeDiscountService.calculateDiscountedPrice(employee, orderAmount);
    }
}