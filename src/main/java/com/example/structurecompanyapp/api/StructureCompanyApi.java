package com.example.structurecompanyapp.api;

import com.example.structurecompanyapp.dao.entity.Company;
import com.example.structurecompanyapp.service.StructureCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/structure/companies")
@Tag(name = "StructureCompany", description = "Structure Company API")
public class StructureCompanyApi {

    private StructureCompanyService structureCompanyService;

    @Autowired
    public StructureCompanyApi(StructureCompanyService structureCompanyService) {
        this.structureCompanyService = structureCompanyService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a company", description = "Get a specific company by id")
    private ResponseEntity<Company> getCompanyById(@PathVariable Long id){
        Optional<Company> company = structureCompanyService.getCompanyById(id);
        if (company.isPresent()) {
            return ResponseEntity.ok(company.get()); // return 200
        }
        return ResponseEntity.notFound().build(); // return 404
    }

    @GetMapping
    @Operation(summary = "Get all companies", description = "Retrieves a list of all companies")
    private ResponseEntity<List<Company>> getAllCompanies(){
        List<Company> allCompanies = structureCompanyService.getAllCompanies();
        return ResponseEntity.ok(allCompanies);
    }

    @PostMapping
    @Operation(summary = "Create a new company", description = "Creates a new company with departments, teams, projects and manager")
    private ResponseEntity<Company> createCompany(@RequestBody Company company){
       Company newCompany = structureCompanyService.createCompany(company);
       return ResponseEntity.status(201).body(newCompany);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a company", description = "Updates an existing company")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company companyDetails) {
        Company updatedCompany = structureCompanyService.updateCompany(id, companyDetails);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a company", description = "Deletes a specific company with departments, teams, project, manager by id")
    private ResponseEntity deleteCompany(@PathVariable Long id){
        if (!structureCompanyService.existCompanyById(id)) {
            return ResponseEntity.notFound().build(); // return 404 Not Found
        }
        structureCompanyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
