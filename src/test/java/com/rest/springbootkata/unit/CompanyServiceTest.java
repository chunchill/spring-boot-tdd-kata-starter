package com.rest.springbootkata.unit;

import com.rest.springbootkata.entity.Company;
import com.rest.springbootkata.entity.Employee;
import com.rest.springbootkata.repository.CompanyRepository;
import com.rest.springbootkata.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {
    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    public void should_return_all_companies_when_find_all_given_companies(){
        //given
        List<Employee> employees1 = new ArrayList<>();
        employees1.add(new Employee(1, "lili", 20, "Female", 2000));
        employees1.add(new Employee(2, "coco", 22, "Female", 8000));

        List<Employee> employees2 = new ArrayList<>();
        employees2.add(new Employee(3, "allen", 20, "Male", 2000));
        employees2.add(new Employee(4, "bob", 22, "Male", 8000));

        Company company1 = new Company(1,"Spring", employees1);
        Company company2 = new Company(2,"Boot", employees2);

        List<Company> companies = new ArrayList<>(Arrays.asList(company1,company2));

        given(companyRepository.findAll()).willReturn(companies);

        //when
        List<Company> actualCompanies = companyService.findAll();

        //then
        verify(companyRepository).findAll();
        assertThat(actualCompanies, hasSize(2));
        assertThat(actualCompanies.get(0), equalTo(company1));
        assertThat(actualCompanies.get(1), equalTo(company2));
    }

    @Test
    public void should_return_company_when_update_given_a_company(){
        //given
        String companyName = "POL";
        List<Employee> employees1 = new ArrayList<>();
        employees1.add(new Employee(1, "lili", 20, "Female", 2000));
        employees1.add(new Employee(2, "coco", 22, "Female", 8000));

        List<Employee> employees2 = new ArrayList<>();
        employees2.add(new Employee(3, "allen", 20, "Male", 2000));
        employees2.add(new Employee(4, "bob", 22, "Male", 8000));

        Company originalCompany = new Company(1,"Spring", employees1);
        Company toUpdateCompany = new Company(2,companyName, employees2);

        int id = originalCompany.getId();
        given(companyRepository.findById(id)).willReturn(originalCompany);

        //when
        Company actualCompany = companyService.update(id, toUpdateCompany);

        //then
        verify(companyRepository).findById(id);
        assertThat(actualCompany.getName(), equalTo(companyName));
    }

    @Test
    public void should_return_a_right_company_when_find_by_id_given_a_id(){
        // given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "lili", 20, "Female", 2000));
        employees.add(new Employee(2, "coco", 22, "Female", 8000));

        Company company = new Company(1,"Spring", employees);
        int id = company.getId();

        given(companyRepository.findById(id)).willReturn(company);

        // when
        Company actualCompany = companyService.findById(id);

        // then
        verify(companyRepository).findById(id);
        assertThat(actualCompany, equalTo(company));
    }

    @Test
    public void should_return_a_company_when_add_given_a_company(){
        // given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "lili", 20, "Female", 2000));
        employees.add(new Employee(2, "coco", 22, "Female", 8000));

        Company originalCompany = new Company(1,"Spring", employees);

        Company createdCompany = new Company(15,"Spring", employees);

        given(companyRepository.create(originalCompany)).willReturn(createdCompany);

        // when
        Company actualCompany = companyService.create(originalCompany);

        // then
        verify(companyRepository).create(originalCompany);
        assertThat(actualCompany, equalTo(createdCompany));
    }
    
    @Test
    public void should_delete_a_company_when_delete_given_a_id(){
        //given
        Integer companyId = 1;

        //when
        companyService.delete(companyId);

        //then
        verify(companyRepository).delete(companyId);
    }

    @Test
    public void should_return_two_right_companies_when_find_by_page_given_5_companies_and_page_2_and_page_size_2(){
        //given
        List<Employee> employees1 = new ArrayList<>();
        employees1.add(new Employee(1, "lili", 20, "Female", 2000));
        employees1.add(new Employee(2, "coco", 22, "Female", 8000));

        List<Employee> employees2 = new ArrayList<>();
        employees2.add(new Employee(3, "allen", 20, "Male", 2000));
        employees2.add(new Employee(4, "bob", 22, "Male", 8000));

        Company company1 = new Company(1,"Spring", employees1);
        Company company2 = new Company(2,"Boot", employees2);

        List<Company> companies = new ArrayList<>(Arrays.asList(company1,company2));

        int page = 2;
        int pageSize = 2;

        given(companyRepository.findByPage(2, 2)).willReturn(companies);

        //when
        List<Company> actualCompanies = companyService.findByPage(page, pageSize);

        //then
        verify(companyRepository).findByPage(2, 2);
        assertThat(actualCompanies, hasSize(2));
        assertThat(actualCompanies.get(0), equalTo(company1));
        assertThat(actualCompanies.get(1), equalTo(company2));
    }
    @Test
    public void should_return_employees_when_find_employees_by_company_id_given_a_id(){
        //given
        Employee employee1 = new Employee(1, "lili", 20, "Female", 2000);
        Employee employee2 = new Employee(2, "coco", 22, "Female", 8000);
        List<Employee> employees = new ArrayList<>(Arrays.asList(employee1, employee2));

        Company company = new Company(1,"Spring", employees);
        int id = company.getId();

        given(companyRepository.findById(id)).willReturn(company);

        //when
        List<Employee> actualEmployees = companyService.getEmployees(id);

        //then
        verify(companyRepository).findById(id);
        assertThat(actualEmployees, hasSize(2));
        assertThat(actualEmployees.get(0), equalTo(employee1));
        assertThat(actualEmployees.get(1), equalTo(employee2));
    }
}
