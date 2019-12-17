package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CodeTask;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeTaskRepository extends JpaRepository<CodeTask, Long> {

    @Query
    List<CodeTask> findTasksByStreams(Stream stream);

    @Query
    List<CodeTask> findTasksByStreamsAndComplexityAndIsEnabled(Stream stream, ComplexityEnum complexityEnum, Boolean enabled);
}
