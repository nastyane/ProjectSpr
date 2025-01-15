package com.vk.itmo.repository;

import com.vk.itmo.data.entities.TicketEntity;
import com.vk.itmo.dto.enums.TicketStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends CrudRepository<TicketEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE TicketEntity m SET m.status = :status WHERE m.id = :ticketId")
    void updateMilestoneStatus(@Param("ticketId") UUID ticketId, @Param("status") TicketStatus status);

    List<TicketEntity> findByMilestoneId(UUID milestoneId);

}