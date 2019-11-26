package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CodeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeTaskRepository extends JpaRepository<CodeTask, Long> {
}
