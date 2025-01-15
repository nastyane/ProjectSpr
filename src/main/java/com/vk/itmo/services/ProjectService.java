package com.vk.itmo.services;

import com.vk.itmo.data.entities.EmployerEntity;
import com.vk.itmo.data.entities.MilestoneEntity;
import com.vk.itmo.data.entities.ProjectEntity;
import com.vk.itmo.data.entities.TestProject;
import com.vk.itmo.dto.*;
import com.vk.itmo.dto.enums.MilestoneStatus;
import com.vk.itmo.dto.enums.RoleType;
import com.vk.itmo.dto.enums.TicketStatus;
import com.vk.itmo.exceptions.*;
import com.vk.itmo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MilestoneRepository milestoneRepository;
    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final TestProjectRepository testProjectRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public ProjectService(UserRepository userRepository,
                          ProjectRepository projectRepository,
                          MilestoneRepository milestoneRepository,
                          EmployerRepository employerRepository,
                          TestProjectRepository testProjectRepository,
                          TicketRepository ticketRepository
    ) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.milestoneRepository = milestoneRepository;
        this.employerRepository = employerRepository;
        this.testProjectRepository = testProjectRepository;
        this.ticketRepository = ticketRepository;
    }


    public CreatedProjectDto createProject(Long currentUserId, NewProjectDto newProjectDto) throws AlreadyExistBoardAlreadyExistsException {
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.getByProjectName(newProjectDto.projectName());
        if (optionalProjectEntity.isPresent()) {
            throw new AlreadyExistBoardAlreadyExistsException();
        }

        ProjectEntity entity = new ProjectEntity(
                currentUserId,
                newProjectDto.projectName(),
                newProjectDto.description()
        );

        ProjectEntity result = projectRepository.save(entity);
        return new CreatedProjectDto(result.getId().toString());
    }

    public void setTeamLeader(Long currentUserId, String projectId, NewProjectUserRoleDto userDto)
            throws AlreadyNotFoundException,
            PermissionException,
            AlreadyExistBoardCantBeSettedException {
        var projectUUID = UUID.fromString(projectId);
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectUUID);
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);

        if (!project.getManagerId().equals(currentUserId)) {
            throw new PermissionException();
        }

        String userName = userDto.userId();
        var userEntity = userRepository.findFirstByUsername(userName).orElseThrow(AlreadyExistBoardCantBeSettedException::new);
        Long userId = userEntity.getId();
        var entity = new EmployerEntity(projectUUID, userId, RoleType.TEAM_LEADER);
        employerRepository.save(entity);
    }


    public void setDeveloper(Long currentUserId, String projectId, NewProjectUserRoleDto userDto) throws AlreadyNotFoundException, PermissionException, AlreadyExistBoardCantBeSettedException {
        var projectUUID = UUID.fromString(projectId);
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectUUID);
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);

        if (!project.getManagerId().equals(currentUserId)) {
            throw new PermissionException();
        }
        String userName = userDto.userId();
        var userEntity = userRepository.findFirstByUsername(userName).orElseThrow(AlreadyExistBoardCantBeSettedException::new);
        Long userId = userEntity.getId();

        var entity = new EmployerEntity(projectUUID, userId, RoleType.DEVELOPER);
        employerRepository.save(entity);
    }

    public void setTester(Long currentUserId, String projectId, NewProjectUserRoleDto userDto)
            throws AlreadyNotFoundException,
            PermissionException,
            AlreadyExistBoardCantBeSettedException {
        var projectUUID = UUID.fromString(projectId);
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectUUID);
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);

        if (!project.getManagerId().equals(currentUserId)) {
            throw new PermissionException();
        }
        String userName = userDto.userId();
        var userEntity = userRepository.findFirstByUsername(userName).orElseThrow(AlreadyExistBoardCantBeSettedException::new);
        Long userId = userEntity.getId();

        var entity = new EmployerEntity(projectUUID, userId, RoleType.TESTER);
        employerRepository.save(entity);
    }

    public CreatedMilestoneDto createMilestones(Long currentUserId, String projectId, NewMilestoneDto milestoneDto)
            throws AlreadyNotFoundException,
            PermissionException,
            AlreadyExistBoardCantBeSettedException {
        var projectUUID = UUID.fromString(projectId);
        ProjectEntity project = projectRepository.findById(projectUUID)
                .orElseThrow(AlreadyNotFoundException::new);

        if (!project.getManagerId().equals(currentUserId)) {
            throw new PermissionException();
        }

        if (milestoneRepository.findActiveMilestoneByProjectId(projectUUID).isPresent()) {
            throw new AlreadyExistBoardCantBeSettedException();
        }
        var entity = new MilestoneEntity(projectUUID, milestoneDto.projectName(), milestoneDto.startDate(), milestoneDto.endDate(), MilestoneStatus.OPEN);
        var result = milestoneRepository.save(entity);
        return new CreatedMilestoneDto(result.getId().toString());
    }

    public void updateMilestoneStatus(Long currentUserId, UUID milestoneId, MilestoneStatusDto statusDto)
            throws AlreadyNotFoundException,
            PermissionException,
            ReportStatusIncorrectException,
            AlreadyNotFoundException {
        MilestoneEntity milestoneEntity = milestoneRepository.findById(milestoneId)
                .orElseThrow(AlreadyNotFoundException::new);
 // TODO добавить текст ошибки что конкретно не было найдено

        ProjectEntity project = projectRepository.findById(milestoneEntity.getProjectId())
                .orElseThrow(AlreadyNotFoundException::new);

        if (!project.getManagerId().equals(currentUserId)) {
            throw new PermissionException();
        }
        var activeMilestoneOptional = milestoneRepository.findActiveMilestoneByProjectId(project.getId());

        switch (statusDto.status()) {
            case OPEN, ACTIVE -> {
                if (activeMilestoneOptional.isPresent() && !activeMilestoneOptional.get().getId().equals(milestoneId)) {
                    throw new ReportStatusIncorrectException();
                }
                milestoneRepository.updateMilestoneStatus(milestoneId, statusDto.status());
            }
            case CLOSED -> {
                if (ticketRepository.findByMilestoneId(milestoneId)
                        .stream()
                        .anyMatch(ticket -> ticket.getStatus() != TicketStatus.COMPLETED)
                ) {
                    throw new ReportStatusIncorrectException();
                }
                milestoneRepository.updateMilestoneStatus(milestoneId, statusDto.status());
            }
        }
    }

    public void testProject(Long currentUserId, String projectIdInput) throws AlreadyNotFoundException, PermissionException {
        UUID projectId;
        try {
            projectId = UUID.fromString(projectIdInput);
        } catch (Exception e) {
            throw new AlreadyNotFoundException();
        }

        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectId);
        optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);

        List<EmployerEntity> employers = employerRepository.findByProjectId(projectId);
        EmployerEntity user = employers.stream()
                .filter(employer -> employer.getUserId().equals(currentUserId))
                .findFirst()
                .orElseThrow(PermissionException::new);

        if (user.getRole() != RoleType.TESTER) {
            throw new PermissionException();
        }

        var entity = new TestProject(projectId, currentUserId, LocalDateTime.now());
        testProjectRepository.save(entity);
    }
}
