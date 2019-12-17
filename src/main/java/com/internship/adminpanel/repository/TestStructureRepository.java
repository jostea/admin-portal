package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.TestStructure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestStructureRepository extends JpaRepository<TestStructure, Long> {
    void deleteTestStructuresByStream(Stream stream);
}
