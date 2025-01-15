package com.vk.itmo.repository;

import com.vk.itmo.data.entities.ConstraintTicketDeveloperKey;
import com.vk.itmo.data.entities.DeveloperTicketAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeveloperTicketAssignmentRepository extends JpaRepository<DeveloperTicketAssignment, ConstraintTicketDeveloperKey> {


    List<DeveloperTicketAssignment> findAllByUserId(Long userId);
}