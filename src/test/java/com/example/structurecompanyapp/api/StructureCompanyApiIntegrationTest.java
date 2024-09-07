package com.example.structurecompanyapp.api;

import com.example.structurecompanyapp.dao.StructureCompanyRepo;
import com.example.structurecompanyapp.dao.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class StructureCompanyApiIntegrationTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private List<Company> companies;

    @Autowired
    private StructureCompanyRepo structureCompanyRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        cleanDatabaseAndResetId();
        structureCompanyRepo.deleteAll();

        companies =  new ArrayList<>();
        Manager manager = Manager.builder().id(1L).name("Alice Johnson").contactInformation("+48 606 932 390").build();
        Project project = Project.builder().id(1L).name("AI Platform").manager(manager).build();
        Team team = Team.builder().id(1L).name("Development Team").project(project).build();
        Department department = Department.builder().id(1L).name("IT Department").teams(Collections.singletonList(team)).build();
        Company company = Company.builder().id(1L).name("Tech Company").departments(Collections.singletonList(department)).build();
        setDependency( company,  department,  team,  project,  manager);
        companies.add(company);
        structureCompanyRepo.save(company);

        manager = Manager.builder().id(2L).name("John Williams").contactInformation("e-mail: john.williams@techcompany.com").build();
        project = Project.builder().id(2L).name("Online exam system").manager(manager).build();
        team = Team.builder().id(2L).name("Workflow Team").project(project).build();
        department = Department.builder().id(2L).name("Development").teams(Collections.singletonList(team)).build();
        company = Company.builder().id(2L).name("Oracle Company").departments(Collections.singletonList(department)).build();

        setDependency( company,  department,  team,  project,  manager);
        companies.add(company);
        structureCompanyRepo.save(company);

        manager = Manager.builder().id(3L).name("Albert Evans").contactInformation("e-mail: albert.evans@techcompany.com").build();
        project = Project.builder().id(3L).name("Education System").manager(manager).build();
        team = Team.builder().id(3L).name("Development Team").project(project).build();
        department = Department.builder().id(3L).name("Management").teams(Collections.singletonList(team)).build();
        company = Company.builder().id(3L).name("IBM Company").departments(Collections.singletonList(department)).build();
        setDependency( company,  department,  team,  project,  manager);
        companies.add(company);
        structureCompanyRepo.save(company);

    }

    private void setDependency(Company company, Department department, Team team, Project project, Manager manager){
        department.setCompany(company);
        team.setDepartment(department);
        project.setTeam(team);
        manager.setProject(project);
    }
    private void cleanDatabaseAndResetId() {
        jdbcTemplate.execute("TRUNCATE TABLE companies RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE departments RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE teams RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE projects RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE managers RESTART IDENTITY CASCADE");
    }

    @Test
    void createCompany_success() throws Exception {


        mockMvc.perform(post("/api/structure/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companies.get(0))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Tech Company")));

        mockMvc.perform(post("/api/structure/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companies.get(1))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Oracle Company")));
    }

    @Test
    void getCompanyById_success() throws Exception {
        Long companyId = companies.get(0).getId();

        mockMvc.perform(get("/api/structure/companies/{id}", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tech Company")));
    }

    @Test
    void getCompanyById_notFound() throws Exception {
        mockMvc.perform(get("/api/structure/companies/{id}", 100000L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCompanies_success() throws Exception {
        mockMvc.perform(get("/api/structure/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name", is("Oracle Company")));
    }



    @Test
    void updateCompany_success() throws Exception {
        Long companyId = companies.get(0).getId();

        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Tech Company");

        mockMvc.perform(put("/api/structure/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCompany)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Tech Company")));
    }

    @Test
    void deleteCompany_success() throws Exception {
        Long companyId = companies.get(1).getId();

        mockMvc.perform(delete("/api/structure/companies/{id}", companyId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCompany_notFound() throws Exception {
        mockMvc.perform(delete("/api/structure/companies/{id}", 100000L))
                .andExpect(status().isNotFound());
    }

}
