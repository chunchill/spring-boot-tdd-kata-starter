package com.rest.springbootkata.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.springbootkata.entity.Employee;
import com.rest.springbootkata.repository.JpaEmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    MockMvc client;

//    @Autowired
//    EmployeeRepository employeeRepository;

    @Autowired
    JpaEmployeeRepository jpaEmployeeRepository;

    @BeforeEach
    void cleanRepository() {
//        System.out.println("clean repository");
//        employeeRepository.clearAll();
       jpaEmployeeRepository.deleteAll();
    }

    @Test
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
        //given
        jpaEmployeeRepository.save(new Employee(10, "Susan", 22, "Female", 10000));

        //when & then
        // [  {"id":2,"name":"Susan","age":22,"gender":"Female","salary":10000}  ]
        client.perform(MockMvcRequestBuilders.get("/employees")) // http status 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1))) // check the size of array
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber()) // index of first element is 0
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan")) // name
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000))
                .andDo(print());
    }

    @Test
    void should_employee_by_id_when_perform_get_given_employees() throws Exception {
        //given
        Employee created = jpaEmployeeRepository.save(new Employee(10, "Susan", 22, "Female", 10000));

        //when & then
        // { "id":2,"name":"Susan","age":22,"gender":"Female","salary":10000 } // json object
        client.perform(MockMvcRequestBuilders.get("/employees/{id}", created.getId())) // http status 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan")) // name
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
    }

    @Test
    void should_return_employees_when_perform_get_by_gender_given_employees() throws Exception {
        //given
        jpaEmployeeRepository.save(new Employee(10, "Susan", 22, "Female", 10000));
        jpaEmployeeRepository.save(new Employee(11, "Leo", 25, "Male", 9000));
        jpaEmployeeRepository.save(new Employee(12, "Robert", 20, "Male", 8000));

        //when & then
//        [
//                {"id":3,"name":"Leo","age":25,"gender":"Male","salary":9000},
//                {"id":4,"name":"Robert","age":20,"gender":"Male","salary":8000}
//        ]
        client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", "Male")) // http status 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Leo", "Robert")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder( 20, 25)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder( "Male", "Male")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder( 9000, 8000)));
    }

    @Test
    void should_return_employees_when_perform_get_by_page_given_employees() throws Exception {
        //given
        jpaEmployeeRepository.save(new Employee(10, "Susan", 22, "Female", 10000));
        jpaEmployeeRepository.save(new Employee(11, "Leo", 25, "Male", 9000));
        jpaEmployeeRepository.save(new Employee(12, "Robert", 20, "Male", 8000));

        //when & then
//        [
//                {"id":2,"name":"Susan","age":22,"gender":"Female","salary":10000}
//                ,{"id":3,"name":"Leo","age":25,"gender":"Male","salary":9000}
//                ]
        client.perform(MockMvcRequestBuilders.get("/employees?page={page}&pageSize={pageSize}", 1, 2)) // http status 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Leo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(22, 25)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(9000, 10000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Female", "Male")));
    }

    @Test
    void should_return_updated_employee_when_perform_put_given_employee() throws Exception {
        //given
        //    requirement: update age and salary
        Employee employee = jpaEmployeeRepository.save(new Employee(10, "Susan", 22, "Female", 10000));
        Employee updateEmployee = new Employee(10, "Jim", 20, "Male", 55000);

        String updateEmployeeJson = new ObjectMapper().writeValueAsString(updateEmployee);

        //when
        client.perform(MockMvcRequestBuilders.put("/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEmployeeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(55000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"));

        // then
        //        assertThat(employees, hasSize(1));
        final Employee updatedEmployee = jpaEmployeeRepository.findAll().get(0);
        assertThat(updatedEmployee.getName(), equalTo("Susan"));
        assertThat(updatedEmployee.getAge(), equalTo(20));
        assertThat(updatedEmployee.getSalary(), equalTo(55000));
        assertThat(updatedEmployee.getGender(), equalTo("Female"));

    }

    @Test
    void should_create_new_employee_when_perform_post_given_new_employee() throws Exception {
        //given
        Employee newEmployee = new Employee(1, "Jim", 20, "Male", 55000);
        String newEmployeeJson = new ObjectMapper().writeValueAsString(newEmployee);

        //when
        client.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newEmployeeJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jim"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(55000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"));

        // then
        List<Employee> employees = jpaEmployeeRepository.findAll();
        assertThat(employees, hasSize(1));
        assertThat(employees.get(0).getName(), equalTo("Jim"));
        assertThat(employees.get(0).getAge(), equalTo(20));
        assertThat(employees.get(0).getSalary(), equalTo(55000));
        assertThat(employees.get(0).getGender(), equalTo("Male"));

    }

    @Test
    void should_return_204_when_perform_delete_given_employee() throws Exception {
        //given
        Employee createdEmployee = jpaEmployeeRepository.save(new Employee(1, "Jim", 20, "Male", 55000));

        //when
        client.perform(MockMvcRequestBuilders.delete("/employees/{id}" , createdEmployee.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //then
        assertThat(jpaEmployeeRepository.findAll(), empty());
    }

    @Test
    void should_return_404_when_perform_get_by_id_given_id_not_exist() throws Exception {
        // given

        // when
        client.perform(MockMvcRequestBuilders.get("/employees/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // then
    }

    @Test
    void should_return_404_when_perform_put_by_id_given_id_not_exist() throws Exception {
        // given
        Employee updateEmployee = new Employee(1, "Jim", 20, "Male", 55000);
        String updateEmployeeJson = new ObjectMapper().writeValueAsString(updateEmployee);

        // when
        client.perform(MockMvcRequestBuilders.put("/employees/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEmployeeJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // then
    }

    @Test
    void should_return_404_when_perform_delete_by_id_given_id_not_exist() throws Exception {
        // given

        // when
        client.perform(MockMvcRequestBuilders.delete("/employees/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // then
    }

}
