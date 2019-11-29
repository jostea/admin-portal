package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRepository extends JpaRepository<Skill, Long> {
}
