package com.vk.itmo.repository;

import com.vk.itmo.data.entities.BugReportEntity;
import com.vk.itmo.dto.enums.BugreportStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BugReportRepository extends CrudRepository<BugReportEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE BugReportEntity m SET m.status = :status WHERE m.id = :bugReportId")
    void updateMilestoneStatus(@Param("bugReportId") UUID bugReportId, @Param("status") BugreportStatus status);
}