package com.example.structurecompanyapp.dao;

import com.example.structurecompanyapp.dao.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructureCompanyRepo extends JpaRepository<Company, Long> {
}

