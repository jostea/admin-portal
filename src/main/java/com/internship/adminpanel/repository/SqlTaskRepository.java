package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.SqlTask;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlTaskRepository  extends JpaRepository<SqlTask, Long> {

    @Query
    List<SqlTask> findTasksByStreams(Stream stream);

    @Query
    List<SqlTask> findTasksByStreamsAndComplexityAndIsEnabled(Stream stream, ComplexityEnum complexityEnum, Boolean enabled);
}
