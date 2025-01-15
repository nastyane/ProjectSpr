package com.vk.itmo.repository;

import com.vk.itmo.data.entities.ConstraintTestProjectKey;
import com.vk.itmo.data.entities.TestProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestProjectRepository extends JpaRepository<TestProject, ConstraintTestProjectKey> {

}