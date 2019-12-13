package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.Task;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query
    List<Task> findTasksByStreams(Stream stream);

    @Query()
    List<Task> findTasksByStreamsAndComplexityAndTaskTypeAndIsEnabled(Stream stream, ComplexityEnum complexityEnum, TypeEnum typeEnum, Boolean enabled);

}
