package com.vk.itmo.repository;

import com.vk.itmo.data.entities.MilestoneEntity;
import com.vk.itmo.dto.enums.MilestoneStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MilestoneRepository extends CrudRepository<MilestoneEntity, UUID> {

    @Query("SELECT m FROM MilestoneEntity m WHERE m.projectId = :projectId AND m.status <> 'CLOSED'")
    Optional<MilestoneEntity> findActiveMilestoneByProjectId(@Param("projectId") UUID projectId);

    @Modifying
    @Transactional // TODO Точно ли нужно @Transactional и какой уровень? вроде один запрос и так уже транзакция
    @Query("UPDATE MilestoneEntity m SET m.status = :status WHERE m.id = :milestoneId")
    void updateMilestoneStatus(@Param("milestoneId") UUID milestoneId, @Param("status") MilestoneStatus status);

}