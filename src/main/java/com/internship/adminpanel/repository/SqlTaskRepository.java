package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.SqlTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlTaskRepository  extends JpaRepository<SqlTask, Long> {
}
