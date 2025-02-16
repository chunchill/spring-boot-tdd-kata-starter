package com.rest.springbootkata.unit;

import com.rest.springbootkata.entity.Employee;
import com.rest.springbootkata.entity.MartMember;
import com.rest.springbootkata.service.MartEmployeeDiscountService;
import com.rest.springbootkata.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void given_regular_user_and_not_birthday_when_place_order_then_no_discount() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Regular");
        martMember.setBirthday(false);
        employee.setMartMember(martMember);

        double result = orderService.placeOrder(employee, 100);
        assertThat(result, equalTo(100.0));
    }

    @Test
    void given_regular_user_and_birthday_when_place_order_then_5_percent_discount() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Regular");
        martMember.setBirthday(true);
        employee.setMartMember(martMember);

        double result = orderService.placeOrder(employee, 100);
        assertThat(result, equalTo(95.0));
    }

    @Test
    void given_member_user_and_order_amount_less_than_100_and_not_birthday_when_place_order_then_10_percent_discount() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Member");
        martMember.setBirthday(false);
        employee.setMartMember(martMember);

        double result = orderService.placeOrder(employee, 100);
        assertThat(result, equalTo(90.0));
    }

    @Test
    void given_member_user_and_order_amount_less_than_100_and_birthday_when_place_order_then_10_percent_plus_5_percent_discount() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Member");
        martMember.setBirthday(true);
        employee.setMartMember(martMember);

        double result = orderService.placeOrder(employee, 100);
        assertThat(result, equalTo(85.5));
    }

    @Test
    void given_member_user_and_order_amount_between_100_and_500_and_not_birthday_when_place_order_then_20_percent_discount() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Member");
        martMember.setBirthday(false);
        employee.setMartMember(martMember);

        double result = orderService.placeOrder(employee, 200);
        assertThat(result, equalTo(160.0));
    }

    @Test
    void given_member_user_and_order_amount_between_100_and_500_and_birthday_when_place_order_then_20_percent_plus_5_percent_discount() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Member");
        martMember.setBirthday(true);
        employee.setMartMember(martMember);

        double result = orderService.placeOrder(employee, 200);
        assertThat(result, equalTo(152.0));
    }

    @Test
    void given_member_user_and_order_amount_greater_than_500_and_not_birthday_when_place_order_then_30_percent_discount() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Member");
        martMember.setBirthday(false);
        employee.setMartMember(martMember);

        double result = orderService.placeOrder(employee, 600);
        assertThat(result, equalTo(420.0));
    }

    @Test
    void given_member_user_and_order_amount_greater_than_500_and_birthday_when_place_order_then_30_percent_plus_5_percent_discount() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Member");
        martMember.setBirthday(true);
        employee.setMartMember(martMember);

        double result = orderService.placeOrder(employee, 600);
        assertThat(Math.round(result), equalTo(399L));
    }

    @Test
    void given_invalid_member_type_when_place_order_then_throw_exception() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Invalid");
        martMember.setBirthday(false);
        employee.setMartMember(martMember);

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(employee, 100));
    }

    @Test
    void given_null_member_type_when_place_order_then_throw_exception() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType(null);
        martMember.setBirthday(false);
        employee.setMartMember(martMember);

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(employee, 100));
    }

    @Test
    void given_negative_order_amount_when_place_order_then_throw_exception() {
        Employee employee = new Employee();
        MartMember martMember = new MartMember();
        martMember.setMemberType("Member");
        martMember.setBirthday(false);
        employee.setMartMember(martMember);

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(employee, -100));
    }
}