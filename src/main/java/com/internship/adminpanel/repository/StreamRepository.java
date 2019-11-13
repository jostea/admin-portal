package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Long> {

    Stream findByName(String name);
}
