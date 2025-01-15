package com.vk.itmo.repository;

import com.vk.itmo.data.entities.ConstraintEmploymentKey;
import com.vk.itmo.data.entities.EmployerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerEntity, ConstraintEmploymentKey> {

    List<EmployerEntity> findByProjectId(UUID projectId);

    List<EmployerEntity> findByUserId(Long userId);
}