package com.rest.springbootkata.service;

import com.rest.springbootkata.entity.Employee;
import com.rest.springbootkata.entity.MartMember;
import org.springframework.stereotype.Service;

@Service
public class MartEmployeeDiscountService {

    public double calculateDiscountedPrice(Employee employee, double orderAmount) {
        if (orderAmount < 0) {
            throw new IllegalArgumentException("Order amount cannot be negative.");
        }

        MartMember martMember = employee.getMartMember();
        if (martMember == null) {
            throw new IllegalArgumentException("MartMember information is missing.");
        }

        double discount = 1.0;

        if ("Regular".equals(martMember.getMemberType())) {
            if (martMember.isBirthday()) {
                discount = 0.95;
            }
        } else if ("Member".equals(martMember.getMemberType())) {
            if (orderAmount <= 100) {
                discount = 0.9;
            } else if (orderAmount <= 500) {
                discount = 0.8;
            } else {
                discount = 0.7;
            }
            if (martMember.isBirthday()) {
                discount *= 0.95;
            }
        } else {
            throw new IllegalArgumentException("Invalid member type.");
        }

        return orderAmount * discount;
    }
}