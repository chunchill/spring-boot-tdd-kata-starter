package com.rest.springbootkata.unit;

import com.rest.springbootkata.entity.Employee;
import com.rest.springbootkata.repository.JpaEmployeeRepository;
import com.rest.springbootkata.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @Mock
    JpaEmployeeRepository jpaEmployeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employees_when_find_all_given_employees() {
        // given
        Employee employee = new Employee(1, "Susan", 22, "Female", 7000);
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(employee);
        given(jpaEmployeeRepository.findAll()).willReturn(employees);

        // when
        List<Employee> result = jpaEmployeeRepository.findAll();

        // should
        verify(jpaEmployeeRepository).findAll();
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(employee));
    }

    @Test
    void should_update_only_age_and_salary_when_update_given_employee() {
        // given
        Integer employeeId = 1;
        String originalName = "Susan";
        String originalGender = "Female";
        Employee originalEmployee = new Employee(1, originalName, 22, originalGender, 10000);
        given(jpaEmployeeRepository.findById(employeeId)).willReturn(Optional.of(originalEmployee));

        Integer newAge = 23;
        Integer newSalary = 10000;
        Employee toUpdateEmployee = new Employee(1, "Tom", newAge, "Male", newSalary);

        // when
        Employee updatedEmployee = employeeService.update(employeeId, toUpdateEmployee);

        // should
        verify(jpaEmployeeRepository).findById(employeeId);
        assertThat(updatedEmployee.getName(), equalTo(originalName));
        assertThat(updatedEmployee.getAge(), equalTo(newAge));
        assertThat(updatedEmployee.getGender(), equalTo(originalGender));
        assertThat(updatedEmployee.getSalary(), equalTo(newSalary));
    }

    @Test
    void should_return_employee_when_find_by_id_given_employee() {
        // given
        Integer employeeId = 1;
        Employee employee = new Employee(1, "Susan", 22, "Female", 7000);
        given(jpaEmployeeRepository.findById(employeeId)).willReturn(Optional.of(employee));

        // when
        Employee result = employeeService.findById(employeeId);

        // should
        verify(jpaEmployeeRepository).findById(employeeId);
        assertThat(result, equalTo(employee));
    }

    @Test
    void should_return_employees_when_find_by_gender_given_employees() {
        // given
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee(1, "Susan", 22, "Female", 7000);
        Employee employee2 = new Employee(2, "Lisa", 20, "Female", 7000);
        Employee employee3 = new Employee(3, "Jim", 21, "Male", 7000);

        String gender = "Female";
        given(jpaEmployeeRepository.findByGender(gender)).willReturn(employees);

        // when
        List<Employee> result = employeeService.findByGender(gender);

        // should
        verify(jpaEmployeeRepository).findByGender(gender);
        assertThat(result, equalTo(employees));
    }

    @Test
    void should_return_employees_when_find_by_page_given_employees() {
        // given
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee(1, "Susan", 22, "Female", 7000);
        Employee employee2 = new Employee(2, "Lisa", 20, "Female", 7000);

        int page = 1;
        int pageSize = 2;
        Page pageObj = new PageImpl(employees);
        final PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        given(jpaEmployeeRepository.findAll(pageRequest)).willReturn(pageObj);

        // when
        List<Employee> result = employeeService.findByPage(page, pageSize);

        // should
        verify(jpaEmployeeRepository).findAll(pageRequest);
        assertThat(result, equalTo(employees));
    }

    @Test
    void should_call_delete_with_specific_id_when_delete_given_an_id() {
        // given
        Integer employeeId = 1;

        // when
        employeeService.delete(employeeId);

        // should
        verify(jpaEmployeeRepository).deleteById(employeeId);
    }

    @Test
    void should_call_create_with_specific_employee_when_create_given_an_employee() {
        // given
        Employee employee = new Employee(1, "Susan", 22, "Female", 7000);
        Employee createdEmployee = new Employee(10, "Susan", 22, "Female", 7000);

        given(jpaEmployeeRepository.save(employee)).willReturn(createdEmployee);

        // when
        Employee result = employeeService.create(employee);

        // should
        verify(jpaEmployeeRepository).save(employee);
        assertThat(result, equalTo(createdEmployee));
    }





}
