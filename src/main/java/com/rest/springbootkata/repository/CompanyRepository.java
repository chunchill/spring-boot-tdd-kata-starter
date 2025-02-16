package com.rest.springbootkata.repository;

import com.rest.springbootkata.entity.Company;
import com.rest.springbootkata.entity.Employee;
import com.rest.springbootkata.exception.NoCompanyFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CompanyRepository {
    private final List<Company> companies = new ArrayList<>();

    public CompanyRepository() {
        ArrayList<Employee> employeesOfCompany1 = new ArrayList<>();
        employeesOfCompany1.add(new Employee(1, "Carlos", 26, "Male", 70000));
        employeesOfCompany1.add(new Employee(2, "Nicole", 22, "Female", 80000));
        companies.add(new Company(100, "spring", employeesOfCompany1));

        ArrayList<Employee> employeesOfCompany2 = new ArrayList<>();
        employeesOfCompany2.add(new Employee(3, "allen", 21, "Female", 90000));
        employeesOfCompany2.add(new Employee(4, "Bob", 20, "Male", 80000));
        companies.add(new Company(101, "summer", employeesOfCompany2));

        ArrayList<Employee> employeesOfCompany3 = new ArrayList<>();
        employeesOfCompany3.add(new Employee(5, "Zoe", 23, "Female", 85000));
        employeesOfCompany3.add(new Employee(6, "Thomas", 22, "Male", 83000));
        companies.add(new Company(102, "autumn", employeesOfCompany3));
    }

    public List<Company> findAll() {
        return companies;
    }

    public List<Company> findByPage(int page, int pageSize) {
        return companies.stream()
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public Company findById(Integer id) {
        return companies.stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .orElseThrow(NoCompanyFoundException::new);
    }

    public void delete(Integer id) {
        Company existingCompany = findById(id);
        companies.remove(existingCompany);
    }

    public List<Employee> getEmployees(Integer id) {
        return findById(id).getEmployees();
    }

    public Company create(Company company) {
        Integer nextId = generateNextId();
        company.setId(nextId);
        companies.add(company);
        return company;
    }

    private Integer generateNextId() {
        int maxId = companies.stream()
                .mapToInt(Company::getId)
                .max()
                .orElse(1);
        return maxId + 1;
    }

    public Company update(Integer id, Company company) {
        Company existingCompany = findById(id);
        if (company.getName() != null) {
            existingCompany.setName(company.getName());
        }
        return existingCompany;
    }

    public void clearAll() {
        companies.clear();
    }
}
