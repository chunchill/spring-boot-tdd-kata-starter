package com.rest.springbootkata.controller;

import com.rest.springbootkata.entity.Company;
import com.rest.springbootkata.entity.Employee;
import com.rest.springbootkata.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<Company> getAll() {
        return companyService.findAll();
    }

    @GetMapping("/{id}")
    public Company getById(@PathVariable Integer id) {
        return companyService.findById(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployees(@PathVariable Integer id) {
        return companyService.getEmployees(id);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Company> getByPage(Integer page, Integer pageSize) {
        return companyService.findByPage(page, pageSize);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        return companyService.create(company);
    }

    @PutMapping("/{id}")
    public Company update(@PathVariable Integer id, @RequestBody Company company) {
        return companyService.update(id, company);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Integer id) {
        companyService.delete(id);
    }
}
