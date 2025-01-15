package com.vk.itmo.services;

import com.vk.itmo.data.entities.*;
import com.vk.itmo.dto.*;
import com.vk.itmo.dto.enums.BugreportStatus;
import com.vk.itmo.dto.enums.RoleType;
import com.vk.itmo.dto.enums.TicketStatus;
import com.vk.itmo.exceptions.*;
import com.vk.itmo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final MilestoneRepository milestoneRepository;
    private final BugReportRepository bugReportRepository;
    private final EmployerRepository employerRepository;
    private final DeveloperTicketAssignmentRepository developerTicketAssignmentRepository;

    @Autowired
    public TicketService(UserRepository userRepository,
                         ProjectRepository projectRepository,
                         MilestoneRepository milestoneRepository,
                         TicketRepository ticketRepository,
                         BugReportRepository bugReportRepository,
                         EmployerRepository employerRepository,
                         DeveloperTicketAssignmentRepository developerTicketAssignmentRepository
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.milestoneRepository = milestoneRepository;
        this.ticketRepository = ticketRepository;
        this.bugReportRepository = bugReportRepository;
        this.employerRepository = employerRepository;
        this.developerTicketAssignmentRepository = developerTicketAssignmentRepository;
    }


    public CreatedTicketDto createTicket(Long currentUserId, UUID milestoneId, NewTicketDto newTicketDto)
            throws AlreadyNotFoundException,
            PermissionException,
            StatusIncorrectException,
            AlreadyExistBoardAlreadyExistsException {
        MilestoneEntity milestoneEntity = milestoneRepository.findById(milestoneId).orElseThrow(AlreadyNotFoundException::new);
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(milestoneEntity.getProjectId());
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);
        List<EmployerEntity> employers = employerRepository.findByProjectId(project.getId());

        if (!project.getManagerId().equals(currentUserId)
                && employers.stream().noneMatch(employer ->
                employer.getUserId().equals(currentUserId) && employer.getRole().equals(RoleType.TEAM_LEADER)
        )) {
            throw new PermissionException();
        }
        var activeMilestone = milestoneRepository.findActiveMilestoneByProjectId(project.getId()).orElseThrow(PermissionException::new);
        if (!activeMilestone.getId().equals(milestoneId)) {
            throw new PermissionException();
        }

        var entity = new TicketEntity(milestoneId, newTicketDto.title(), newTicketDto.description(), TicketStatus.NEW);
        var result = ticketRepository.save(entity);
        return new CreatedTicketDto(result.getId().toString());
    }

    public void assignTicket(Long currentUserId, UUID ticketId, UserAssignDto userDto)
            throws AlreadyNotFoundException,
            PermissionException,
            AlreadyExistBoardCantBeSettedException{
        TicketEntity ticketEntity = ticketRepository.findById(ticketId).orElseThrow(AlreadyNotFoundException::new);
        MilestoneEntity milestoneEntity = milestoneRepository.findById(ticketEntity.getMilestoneId()).orElseThrow(AlreadyNotFoundException::new);
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(milestoneEntity.getProjectId());
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);
        List<EmployerEntity> employers = employerRepository.findByProjectId(project.getId());

        if (!project.getManagerId().equals(currentUserId)
                && employers.stream().noneMatch(employer ->
                employer.getUserId().equals(currentUserId) && employer.getRole().equals(RoleType.TEAM_LEADER)
        )) {
            throw new PermissionException();
        }

        var activeMilestone = milestoneRepository.findActiveMilestoneByProjectId(project.getId()).orElseThrow(AlreadyNotFoundException::new);
        if (!activeMilestone.getId().equals(ticketEntity.getMilestoneId())) {
            throw new AlreadyNotFoundException();
        }

        String userName = userDto.userId();
        var userEntity = userRepository.findFirstByUsername(userName).orElseThrow(AlreadyExistBoardCantBeSettedException::new);
        Long userId = userEntity.getId();
        if (employers.stream().noneMatch(employer -> employer.getUserId().equals(userId) && employer.getRole().equals(RoleType.DEVELOPER))) {
            throw new AlreadyExistBoardCantBeSettedException();
        }

        var entity = new DeveloperTicketAssignment(ticketId, userId);
        developerTicketAssignmentRepository.save(entity);
    }

    public TicketStatusDto getTicketStatus(Long currentUserId, UUID ticketId)
            throws AlreadyNotFoundException,
            PermissionException {
        TicketEntity ticketEntity = ticketRepository.findById(ticketId).orElseThrow(AlreadyNotFoundException::new);
        MilestoneEntity milestoneEntity = milestoneRepository.findById(ticketEntity.getMilestoneId()).orElseThrow(AlreadyNotFoundException::new);
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(milestoneEntity.getProjectId());
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);
        List<EmployerEntity> employers = employerRepository.findByProjectId(project.getId());

        if (!project.getManagerId().equals(currentUserId)
                && employers.stream().noneMatch(employer ->
                employer.getUserId().equals(currentUserId)
                        && (employer.getRole().equals(RoleType.TEAM_LEADER) || employer.getRole().equals(RoleType.DEVELOPER))
        )) {
            throw new PermissionException();
        }

        return new TicketStatusDto(ticketEntity.getStatus());
    }

    public void setTicketStatus(Long currentUserId, UUID ticketId, TicketStatusDto statusDto)
            throws AlreadyNotFoundException,
            PermissionException,
            StatusIncorrectException {
        TicketEntity ticketEntity = ticketRepository.findById(ticketId).orElseThrow(AlreadyNotFoundException::new);
        MilestoneEntity milestoneEntity = milestoneRepository.findById(ticketEntity.getMilestoneId()).orElseThrow(AlreadyNotFoundException::new);
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(milestoneEntity.getProjectId());
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);
        List<EmployerEntity> employers = employerRepository.findByProjectId(project.getId());


        if (employers.stream().noneMatch(employer ->
                employer.getUserId().equals(currentUserId)
                        && (employer.getRole().equals(RoleType.TEAM_LEADER) || employer.getRole().equals(RoleType.DEVELOPER))
        )) {
            throw new PermissionException();
        }

        var activeMilestone = milestoneRepository.findActiveMilestoneByProjectId(project.getId()).orElseThrow(AlreadyNotFoundException::new);
        if (!activeMilestone.getId().equals(ticketEntity.getMilestoneId())) {
            throw new StatusIncorrectException();
        }

        if (!correctTickets.contains(statusDto.status())) {
            throw new StatusIncorrectException();
        }

        ticketRepository.updateMilestoneStatus(ticketId, statusDto.status());
    }

    private final List<TicketStatus> correctTickets = List.of(TicketStatus.ACCEPTED, TicketStatus.COMPLETED, TicketStatus.IN_PROGRESS);

    public CreatedBugReportDto createBugreport(Long currentUserId, UUID projectId, NewBugreportDto bugreportDto)
            throws AlreadyNotFoundException,
            PermissionException {
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectId);
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);
        List<EmployerEntity> employers = employerRepository.findByProjectId(project.getId());

        if (employers.stream().noneMatch(employer ->
                employer.getUserId().equals(currentUserId)
                        && (employer.getRole().equals(RoleType.DEVELOPER) || employer.getRole().equals(RoleType.TESTER))
        )) {
            throw new PermissionException();
        }

        var entity = new BugReportEntity(projectId, bugreportDto.description(), BugreportStatus.OPEN);
        var result = bugReportRepository.save(entity);
        return new CreatedBugReportDto(result.getId().toString());
    }

    public void setBugReportStatus(Long currentUserId, UUID bugReportId, BugreportStatusDto statusDto)
            throws AlreadyNotFoundException,
            PermissionException,
            ReportStatusIncorrectException {
        BugReportEntity bugReportEntity = bugReportRepository.findById(bugReportId).orElseThrow(AlreadyNotFoundException::new);
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(bugReportEntity.getProjectId());
        ProjectEntity project = optionalProjectEntity.orElseThrow(AlreadyNotFoundException::new);
        List<EmployerEntity> employers = employerRepository.findByProjectId(project.getId());

        EmployerEntity user = employers.stream()
                .filter(employer -> employer.getUserId().equals(currentUserId)).findFirst()
                .orElseThrow(PermissionException::new);

        boolean isEnabledChangeStatus = isEnabledChangeStatus(statusDto, user, bugReportEntity);
        if (!isEnabledChangeStatus) throw new ReportStatusIncorrectException();

        bugReportRepository.updateMilestoneStatus(bugReportId, statusDto.status());
    }

    private static boolean isEnabledChangeStatus(BugreportStatusDto statusDto, EmployerEntity user, BugReportEntity bugReportEntity) {
        boolean isEnabledChangeStatus;
        switch (user.getRole()) {
            case DEVELOPER: {
                isEnabledChangeStatus = ((bugReportEntity.getStatus() == BugreportStatus.OPEN) && (statusDto.status() == BugreportStatus.FIXED));
                break;
            }
            case TESTER: {
                isEnabledChangeStatus = ((bugReportEntity.getStatus() == BugreportStatus.OPEN
                        || bugReportEntity.getStatus() == BugreportStatus.FIXED
                        || bugReportEntity.getStatus() == BugreportStatus.TESTED)
                        && (
                        statusDto.status() == BugreportStatus.TESTED
                                || statusDto.status() == BugreportStatus.CLOSED
                ));
                break;
            }
            default: {
                isEnabledChangeStatus = false;
            }
        }
        return isEnabledChangeStatus;
    }
}
