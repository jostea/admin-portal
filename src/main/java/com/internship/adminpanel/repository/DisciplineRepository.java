package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline,Long> {
    List<Discipline> findDisciplineByNameContainingIgnoreCase(String name);
}
