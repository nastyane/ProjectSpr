package com.vk.itmo.controllers;

import com.vk.itmo.dto.*;
import com.vk.itmo.exceptions.*;
import com.vk.itmo.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @PostMapping("/projects")
    private ResponseEntity<CreatedProjectDto> createProject(@AuthenticationPrincipal Long currentUserId,
                                                            @RequestBody NewProjectDto newProjectDto
    ) {
        try {
            CreatedProjectDto dto = projectService.createProject(currentUserId, newProjectDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/projects/{projectId}/teamleader")
    private ResponseEntity<Object> setTeamLeader(@AuthenticationPrincipal Long currentUserId,
                                                 @PathVariable String projectId,
                                                 @RequestBody NewProjectUserRoleDto userDto
    ) {
        try {
            projectService.setTeamLeader(currentUserId, projectId, userDto);
            return ResponseEntity.ok().build();
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/projects/{projectId}/developers")
    private ResponseEntity<Object> setDeveloper(@AuthenticationPrincipal Long currentUserId,
                                                @PathVariable String projectId,
                                                @RequestBody NewProjectUserRoleDto userDto
    ) {
        try {
            projectService.setDeveloper(currentUserId, projectId, userDto);
            return ResponseEntity.ok().build();
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AlreadyExistBoardCantBeSettedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/projects/{projectId}/testers")
    private ResponseEntity<Object> setTester(@AuthenticationPrincipal Long currentUserId,
                                             @PathVariable String projectId,
                                             @RequestBody NewProjectUserRoleDto userDto
    ) {
        try {
            projectService.setTester(currentUserId, projectId, userDto);
            return ResponseEntity.ok().build();
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AlreadyExistBoardCantBeSettedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/projects/{projectId}/milestones")
    private ResponseEntity<CreatedMilestoneDto> createMilestones(@AuthenticationPrincipal Long currentUserId,
                                                                 @PathVariable String projectId,
                                                                 @RequestBody NewMilestoneDto milestoneDto
    ) {
        try {
            CreatedMilestoneDto dto = projectService.createMilestones(currentUserId, projectId, milestoneDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/milestones/{milestoneId}/status")
    private ResponseEntity<Object> updateMilestoneStatus(@AuthenticationPrincipal Long currentUserId,
                                                         @PathVariable UUID milestoneId,
                                                         @RequestBody MilestoneStatusDto statusDto) {
        try {
            projectService.updateMilestoneStatus(currentUserId, milestoneId, statusDto);
            return ResponseEntity.ok().build();
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/projects/{projectId}/test")
    private ResponseEntity<Object> testProject(@AuthenticationPrincipal Long currentUserId, @PathVariable String projectId) {
        try {
            projectService.testProject(currentUserId, projectId);
            return ResponseEntity.ok().build();
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
