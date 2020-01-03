package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.SqlGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlGroupRepository extends JpaRepository<SqlGroup, Long> {
}
