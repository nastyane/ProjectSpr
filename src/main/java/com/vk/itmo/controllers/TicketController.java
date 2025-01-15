package com.vk.itmo.controllers;

import com.vk.itmo.dto.*;
import com.vk.itmo.exceptions.*;
import com.vk.itmo.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TicketController {


    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/milestones/{milestoneId}/tickets")
    private ResponseEntity<CreatedTicketDto> createTicket(@AuthenticationPrincipal Long currentUserId,
                                                          @PathVariable UUID milestoneId,
                                                          @RequestBody NewTicketDto newTicketDto) {
        try {
            var dto = ticketService.createTicket(currentUserId, milestoneId, newTicketDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (StatusIncorrectException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AlreadyExistBoardAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/tickets/{ticketId}/assign")
    private ResponseEntity<Object> assignUserToTicket(@AuthenticationPrincipal Long currentUserId,
                                                      @PathVariable UUID ticketId,
                                                      @RequestBody UserAssignDto userDto) {
        try {
            ticketService.assignTicket(currentUserId, ticketId, userDto);
            return ResponseEntity.ok().build();
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AlreadyExistBoardCantBeSettedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/tickets/{ticketId}/status")
    private ResponseEntity<TicketStatusDto> getTicketStatus(@AuthenticationPrincipal Long currentUserId,
                                                            @PathVariable UUID ticketId) {
        try {
            TicketStatusDto status = ticketService.getTicketStatus(currentUserId, ticketId);
            return ResponseEntity.ok(status);
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AlreadyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/tickets/{ticketId}/status")
    private ResponseEntity<Object> setTicketStatus(@AuthenticationPrincipal Long currentUserId,
                                                   @PathVariable UUID ticketId,
                                                   @RequestBody TicketStatusDto statusDto) {
        try {
            ticketService.setTicketStatus(currentUserId, ticketId, statusDto);
            return ResponseEntity.ok().build();
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AlreadyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (StatusIncorrectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/projects/{projectId}/bugreports")
    private ResponseEntity<CreatedBugReportDto> createBugreport(@AuthenticationPrincipal Long currentUserId,
                                                                @PathVariable UUID projectId,
                                                                @RequestBody NewBugreportDto bugreportDto) {
        try {
            CreatedBugReportDto dto = ticketService.createBugreport(currentUserId, projectId, bugreportDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AlreadyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/bugreports/{bugReportId}/status")
    private ResponseEntity<Object> setBugreportStatus(@AuthenticationPrincipal Long currentUserId,
                                                      @PathVariable UUID bugReportId,
                                                      @RequestBody BugreportStatusDto statusDto) {
        try {
            ticketService.setBugReportStatus(currentUserId, bugReportId, statusDto);
            return ResponseEntity.ok().build();
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AlreadyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ReportStatusIncorrectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
