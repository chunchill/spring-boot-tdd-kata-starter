package com.rest.springbootkata.service;

import com.rest.springbootkata.entity.Company;
import com.rest.springbootkata.entity.Employee;
import com.rest.springbootkata.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public List<Company> findByPage(Integer page, Integer pageSize) {
        return companyRepository.findByPage(page, pageSize);
    }

    public Company findById(Integer companyId) {
        return companyRepository.findById(companyId);
    }

    public Company create(Company company) {
        return companyRepository.create(company);
    }

    public void delete(Integer companyId) {
        companyRepository.delete(companyId);
    }

    public Company update(Integer companyId, Company toUpdateCompany) {
        Company existingCompany = companyRepository.findById(companyId);
        if (toUpdateCompany.getName() != null) {
            existingCompany.setName(toUpdateCompany.getName());
        }
        return existingCompany;
    }

    public List<Employee> getEmployees(Integer companyId) {
        Company company = companyRepository.findById(companyId);
        return company.getEmployees();
    }

}
