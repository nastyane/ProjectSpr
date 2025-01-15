package com.vk.itmo.services;

import com.vk.itmo.data.entities.DeveloperTicketAssignment;
import com.vk.itmo.data.entities.EmployerEntity;
import com.vk.itmo.data.entities.ProjectEntity;
import com.vk.itmo.data.entities.UserEntity;
import com.vk.itmo.dto.*;
import com.vk.itmo.dto.enums.RoleType;
import com.vk.itmo.exceptions.IllegalAlreadyExistBoardCredentialsException;
import com.vk.itmo.exceptions.AlreadyExistBoardAlreadyExistsException;
import com.vk.itmo.exceptions.PermissionException;
import com.vk.itmo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;
    private final DeveloperTicketAssignmentRepository developerTicketAssignmentRepository;
    private final TokenRepository tokenRepository;
    private final EmployerRepository employerRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       TokenRepository tokenRepository,
                       ProjectRepository projectRepository,
                       EmployerRepository employerRepository,
                       TicketRepository ticketRepository,
                       DeveloperTicketAssignmentRepository developerTicketAssignmentRepository
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.projectRepository = projectRepository;
        this.employerRepository = employerRepository;
        this.ticketRepository = ticketRepository;
        this.developerTicketAssignmentRepository = developerTicketAssignmentRepository;
    }

    public void registerUser(UserRegistrationDto registrationDto) throws AlreadyExistBoardAlreadyExistsException {
        if (userRepository.findFirstByUsername(registrationDto.username()).isPresent()) {
            // TODO throw new AlreadyExistBoardException();
            {
                throw new AlreadyExistBoardAlreadyExistsException();
            }
        }

        var entity = new UserEntity(registrationDto.username(), registrationDto.email(), registrationDto.password());
        userRepository.save(entity);
    }

    public TokenDto login(UserLoginDto loginDto) throws IllegalAlreadyExistBoardCredentialsException {
        Optional<UserEntity> userOptional = userRepository.findFirstByUsername(loginDto.username());
        UserEntity user = userOptional.orElseThrow(IllegalAlreadyExistBoardCredentialsException::new);

        if (user.getUsername().equals(loginDto.username()) && user.getPassword().equals(loginDto.password())) {
            String token = tokenRepository.generateToken(user.getId());
            tokenRepository.saveToken(user.getId(), token);
            return new TokenDto(token);
        }

        throw new IllegalAlreadyExistBoardCredentialsException();
    }

    public List<UserProjectDto> getProjects(long currentUserId, long userId) throws PermissionException {
        if (currentUserId != userId) throw new PermissionException();
        var projectsAsEmployer = employerRepository.findByUserId(currentUserId).stream().map(EmployerEntity::getProjectId);
        var projectsAsManager = projectRepository.findByManagerId(userId).stream().map(ProjectEntity::getId);
        var projectsIds = Stream.concat(projectsAsEmployer, projectsAsManager).toList();
        return StreamSupport.stream(projectRepository.findAllById(projectsIds).spliterator(), false)
                .map(project ->
                        new UserProjectDto(project.getId().toString(), project.getProjectName(), project.getDescription())
                ).toList();
    }

    public List<UserTicketDto> getTickets(long currentUserId, long userId) throws PermissionException {
        if (currentUserId != userId) throw new PermissionException();

        var assignedTicketsIds = developerTicketAssignmentRepository.findAllByUserId(currentUserId)
                .stream()
                .map(DeveloperTicketAssignment::getTicketId)
                .toList();
        return StreamSupport.stream(ticketRepository.findAllById(assignedTicketsIds).spliterator(), false)
                .map(ticket ->
                        new UserTicketDto(ticket.getId().toString(), ticket.getTitle(), ticket.getStatus())
                ).toList();
    }

    public List<UserBugreportDto> getBugreports(long currentUserId, long userId) throws PermissionException {
        if (currentUserId != userId) throw new PermissionException();

        var projectsAsEmployer = employerRepository.findByUserId(currentUserId).stream().filter(employer ->
                employer.getRole() == RoleType.DEVELOPER || employer.getRole() == RoleType.TESTER
        ).map(EmployerEntity::getProjectId).toList();
        return StreamSupport.stream(projectRepository.findAllById(projectsAsEmployer).spliterator(), false)
                .map(project ->
                        new UserBugreportDto(project.getId().toString(), project.getProjectName(), project.getDescription())
                ).toList();
    }

}
