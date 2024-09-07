package com.example.structurecompanyapp.service;

import com.example.structurecompanyapp.dao.StructureCompanyRepo;
import com.example.structurecompanyapp.dao.entity.Company;
import com.example.structurecompanyapp.dao.entity.Department;
import com.example.structurecompanyapp.dao.entity.Project;
import com.example.structurecompanyapp.dao.entity.Team;
import com.example.structurecompanyapp.exception.CompanyFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StructureCompanyService {

    private StructureCompanyRepo structureRepo;

    @Autowired
    public void StructureRepo(StructureCompanyRepo structureRepo) {
        this.structureRepo = structureRepo;
    }

    // Create a new Company
    public Company createCompany(Company company) {
        List<Department> departments =  setCompanyForDepartment(company);
        List<Team> teams = setDepartmentForTeam(departments);
        Project project = setTeamForProject(teams);
        setProjectForManager(project);

        return structureRepo.save(company);
    }

    private List<Department> setCompanyForDepartment(Company company){
        return  company.getDepartments()
                .stream()
                .peek(department -> department.setCompany(company))
                .collect(Collectors.toList());
    }
    private List<Team> setDepartmentForTeam(List<Department> departments){
        return  departments.stream()
                .flatMap(department -> department.getTeams().stream())
                .peek(team -> team.setDepartment(team.getDepartment()))
                .collect(Collectors.toList());
    }

    private Project setTeamForProject(List<Team> teams){
        return teams.stream()
                .map(Team::getProject)
                .filter(Objects::nonNull)
                .peek(project -> project.setTeam(project.getTeam()))
                .findFirst().orElse(null);
    }

    private void setProjectForManager(Project project){
        if(project.getManager() != null){
            project.getManager().setProject(project);
        }
    }

    // Get company by id
    public Optional<Company> getCompanyById(Long id) {
        return structureRepo.findById(id);
    }

    // Get true if company exist
    public boolean existCompanyById(Long id) {
        return structureRepo.findById(id).isPresent();
    }
    // Get all companies
    public List<Company> getAllCompanies() {

        List<Company> companyList = new ArrayList<Company>();
        Iterable<Company> companyIterable = structureRepo.findAll();
        companyIterable.forEach(companyList::add);

        return companyList;

    }

    // Update company
    public Company updateCompany(Long id, Company companyDetails) {
        return structureRepo
                .findById(id)
                .map(company -> {
                    company.setName(companyDetails.getName());

                    try{
                        if(company.getDepartments() != null && !company.getDepartments().isEmpty()) {
                            company.getDepartments().clear();
                        }
                    }catch(UnsupportedOperationException ex){
                        List<Department> modifiableDepartments = new ArrayList<>(company.getDepartments());
                        modifiableDepartments.clear();
                        company.setDepartments(modifiableDepartments);
                    }

                    if(companyDetails.getDepartments() != null) {
                        for (Department department : companyDetails.getDepartments()) {
                            department.setCompany(company);
                            company.getDepartments().add(department);
                        }
                    }

                    return structureRepo.save(company);
                })
                .orElseThrow(() -> new CompanyFoundException("Company not found with id " + id));
    }

    // Delete company
    public void deleteCompany(Long id) {
        structureRepo.deleteById(id);
    }
}
