package com.example.structurecompanyapp.service;

import com.example.structurecompanyapp.dao.StructureCompanyRepo;
import com.example.structurecompanyapp.dao.entity.*;
import com.example.structurecompanyapp.exception.CompanyFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StructureCompanyServiceTest {

    @Mock
    private StructureCompanyRepo structureCompanyRepo;

    @InjectMocks
    private StructureCompanyService structureCompanyService;

    private List<Company> companies;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        companies =  new ArrayList<>();
        Manager manager = Manager.builder().id(1L).name("Alice Johnson").contactInformation("+48 606 932 390").build();
        Project project = Project.builder().id(1L).name("AI Platform").manager(manager).build();
        Team team = Team.builder().id(1L).name("Development Team").project(project).build();
        Department department = Department.builder().id(1L).name("IT Department").teams(Collections.singletonList(team)).build();
        Company company = Company.builder().name("Tech Company").id(1L).departments(Collections.singletonList(department)).build();
        companies.add(company);

        manager = Manager.builder().id(2L).name("John Williams").contactInformation("e-mail: john.williams@techcompany.com").build();
        project = Project.builder().id(2L).name("Online exam system").manager(manager).build();
        team = Team.builder().id(2L).name("Workflow Team").project(project).build();
        department = Department.builder().id(2L).name("Development").teams(Collections.singletonList(team)).build();
        company = Company.builder().name("Oracle Company").id(2L).departments(Collections.singletonList(department)).build();
        companies.add(company);
    }


    @Test
    void createCompany_success() {
        Company company = companies.get(0);
        when(structureCompanyRepo.save(any(Company.class))).thenReturn(company);
        Company createdCompany = structureCompanyService.createCompany(company);

        assertEquals("Tech Company", createdCompany.getName());
        verify(structureCompanyRepo, times(1)).save(company);
    }


    @Test
    void getCompanyById_success() {
        Company company = companies.get(1);

        when(structureCompanyRepo.findById(2L)).thenReturn(Optional.of(company));

        Optional<Company> foundCompany = structureCompanyService.getCompanyById(2L);

        assertEquals("Oracle Company", foundCompany.get().getName());
        verify(structureCompanyRepo, times(1)).findById(2L);
    }

    @Test
    void existCompanyById_true() {
        when(structureCompanyRepo.findById(1L)).thenReturn(Optional.of(companies.get(0)));

        boolean exists = structureCompanyService.existCompanyById(1L);

        assertEquals(true, exists);
        verify(structureCompanyRepo, times(1)).findById(1L);
    }

    @Test
    void existCompanyById_false() {
        when(structureCompanyRepo.findById(3L)).thenReturn(Optional.empty());

        boolean exists = structureCompanyService.existCompanyById(3L);

        assertEquals(false, exists);
        verify(structureCompanyRepo, times(1)).findById(3L);
    }

    @Test
    void getAllCompanies() {

        when(structureCompanyRepo.findAll()).thenReturn(companies);
        List<Company> companyList = structureCompanyService.getAllCompanies();

        assertEquals(2, companyList.size());
        assertEquals("Tech Company", companyList.get(0).getName());
        verify(structureCompanyRepo, times(1)).findAll();
    }

    @Test
    void updateCompany_success() {
        Company company = companies.get(0);
        Company modifyCompany = new Company();
        modifyCompany.setName("Updated Company Name");

        when(structureCompanyRepo.findById(1L)).thenReturn(Optional.of(company));
        when(structureCompanyRepo.save(any(Company.class))).thenReturn(company);

        Company updatedCompany = structureCompanyService.updateCompany(1L, modifyCompany);

        assertEquals("Updated Company Name", updatedCompany.getName());
        verify(structureCompanyRepo, times(1)).findById(1L);
        verify(structureCompanyRepo, times(1)).save(company);
    }

    @Test
    void updateCompany_notFound_throwsException() {
        when(structureCompanyRepo.findById(1L)).thenReturn(Optional.empty());

        Company updatedCompanyDetails = new Company();
        updatedCompanyDetails.setName("Updated Company Name");

        assertThrows(CompanyFoundException.class, () -> {
            structureCompanyService.updateCompany(1L, updatedCompanyDetails);
        });

        verify(structureCompanyRepo, times(1)).findById(1L);
        verify(structureCompanyRepo, times(0)).save(any(Company.class));
    }

    @Test
    void deleteCompany() {
        structureCompanyService.deleteCompany(1L);
        verify(structureCompanyRepo, times(1)).deleteById(1L);
    }
}