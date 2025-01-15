package com.vk.itmo.repository;

import com.vk.itmo.data.entities.ProjectEntity;
import com.vk.itmo.data.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, UUID> {

    Optional<ProjectEntity> getByProjectName(String projectName);

    List<ProjectEntity> findByManagerId(Long managerId);
}