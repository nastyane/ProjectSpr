package com.vk.itmo.data.entities;

import com.vk.itmo.dto.enums.MilestoneStatus;
import com.vk.itmo.dto.enums.RoleType;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "milestones")
public class MilestoneEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @NonNull
        private UUID projectId;

        @NonNull
        private String name;

        @NonNull
        private LocalDate startDate;

        @NonNull
        private LocalDate endDate;

        @NonNull
        @Enumerated(EnumType.STRING)
        private MilestoneStatus status;

        protected MilestoneEntity() {}

        public MilestoneEntity(UUID projectId, String name, LocalDate startDate, LocalDate endDate, MilestoneStatus status) {
                this.projectId = projectId;
                this.name = name;
                this.startDate = startDate;
                this.endDate = endDate;
                this.status = status;
        }


        public UUID getId() {
                return id;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public UUID getProjectId() {
                return projectId;
        }

        public void setProjectId(UUID projectId) {
                this.projectId = projectId;
        }

        public MilestoneStatus getStatus() {
                return status;
        }

        public void setStatus(MilestoneStatus status) {
                this.status = status;
        }
}
