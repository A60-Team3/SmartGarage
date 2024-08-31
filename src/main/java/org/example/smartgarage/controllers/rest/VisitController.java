package org.example.smartgarage.controllers.rest;

import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.smartgarage.dtos.VisitOutDto;
import org.example.smartgarage.events.EmailReportEvent;
import org.example.smartgarage.mappers.VisitMapper;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VisitService;
import org.example.smartgarage.utils.filtering.TimeOperator;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/garage")
public class VisitController {
    private final VisitService visitService;
    private final UserService userService;
    private final VisitMapper visitMapper;
    private final ApplicationEventPublisher eventPublisher;

    public VisitController(VisitService visitService, UserService userService, VisitMapper visitMapper, ApplicationEventPublisher eventPublisher) {
        this.visitService = visitService;
        this.userService = userService;
        this.visitMapper = visitMapper;
        this.eventPublisher = eventPublisher;
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/visits")
    public ResponseEntity<?> findAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                     @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                     @Parameter(description = "Partial or full first name")
                                     @RequestParam(required = false) String customerName,
                                     @Parameter(description = "Partial or full last name")
                                     @RequestParam(required = false) String clerkName,
                                     @RequestParam(required = false) TimeOperator scheduleCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateFrom,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateTo,
                                     @RequestParam(required = false) String brandName,
                                     @Parameter(description = "Pattern - String length 17")
                                     @RequestParam(required = false) String vehicleVin,
                                     @Parameter(description = "Pattern - XX XXXX XX")
                                     @RequestParam(required = false) String vehicleRegistry,
                                     @RequestParam(required = false) TimeOperator bookedCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDateTime bookedOn,
                                     @Parameter(description = "Options - all field names")
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) String sortOrder,
                                     @RequestParam boolean toPdf,
                                     @RequestParam(required = false) String exchangeCurrency,
                                     @AuthenticationPrincipal CustomUserDetails principal) throws IOException, DocumentException {
        VisitFilterOptions visitFilterOptions = getVisitFilterOptions(
                null, customerName, null, clerkName,
                scheduleCondition, scheduleDateFrom, scheduleDateTo,
                brandName, vehicleVin, vehicleRegistry,
                bookedCondition, bookedOn, sortBy, sortOrder
        );

        Page<VisitOutDto> visitsPage = getVisitOutDtos(
                offset, pageSize, toPdf,
                exchangeCurrency, principal.getId(), visitFilterOptions
        );

        return ResponseEntity.ok(visitsPage);
    }

    @PreAuthorize("hasRole('CUSTOMER') && #userId == principal.getId()")
    @GetMapping("/users/{userId}/visits")
    public ResponseEntity<?> findAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                     @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                     @Parameter(description = "Logged customer id")
                                     @PathVariable(required = false) Long userId,
                                     @Parameter(description = "Partial or full first name")
                                     @RequestParam(required = false) String customerName,
                                     @Parameter(description = "Partial or full last name")
                                     @RequestParam(required = false) String clerkName,
                                     @RequestParam(required = false) TimeOperator scheduleCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateFrom,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateTo,
                                     @RequestParam(required = false) String brandName,
                                     @Parameter(description = "Pattern - String length 17")
                                     @RequestParam(required = false) String vehicleVin,
                                     @Parameter(description = "Pattern - XX XXXX XX")
                                     @RequestParam(required = false) String vehicleRegistry,
                                     @RequestParam(required = false) TimeOperator bookedCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDateTime bookedOn,
                                     @Parameter(description = "Options - all field names")
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) String sortOrder,
                                     @RequestParam boolean toPdf,
                                     @RequestParam(required = false) String exchangeCurrency,
                                     @AuthenticationPrincipal CustomUserDetails principal) throws IOException, DocumentException {
        VisitFilterOptions visitFilterOptions = getVisitFilterOptions(
                userId, customerName, null, clerkName,
                scheduleCondition, scheduleDateFrom, scheduleDateTo,
                brandName, vehicleVin, vehicleRegistry,
                bookedCondition, bookedOn, sortBy, sortOrder
        );

        Page<VisitOutDto> visitsPage = getVisitOutDtos(
                offset, pageSize, toPdf,
                exchangeCurrency, userId, visitFilterOptions
        );

        return ResponseEntity.ok(visitsPage);
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/clerks/visits")
    public ResponseEntity<?> findAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                     @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                     @Parameter(description = "Logged customer id")
                                     @RequestParam(required = false) Long customerId,
                                     @Parameter(description = "Partial or full first name")
                                     @RequestParam(required = false) String customerName,
                                     @Parameter(description = "Logged customer id")
                                     @RequestParam(required = false) Long clerkId,
                                     @Parameter(description = "Partial or full last name")
                                     @RequestParam(required = false) String clerkName,
                                     @RequestParam(required = false) TimeOperator scheduleCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateFrom,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateTo,
                                     @RequestParam(required = false) String brandName,
                                     @Parameter(description = "Pattern - String length 17")
                                     @RequestParam(required = false) String vehicleVin,
                                     @Parameter(description = "Pattern - XX XXXX XX")
                                     @RequestParam(required = false) String vehicleRegistry,
                                     @RequestParam(required = false) TimeOperator bookedCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDateTime bookedOn,
                                     @Parameter(description = "Options - all field names")
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) String sortOrder,
                                     @RequestParam boolean toPdf,
                                     @RequestParam(required = false) String exchangeCurrency,
                                     @AuthenticationPrincipal CustomUserDetails principal) throws IOException, DocumentException {
        VisitFilterOptions visitFilterOptions = getVisitFilterOptions(
                customerId, customerName, clerkId, clerkName,
                scheduleCondition, scheduleDateFrom, scheduleDateTo,
                brandName, vehicleVin, vehicleRegistry,
                bookedCondition, bookedOn, sortBy, sortOrder
        );


        Page<VisitOutDto> visitsPage = getVisitOutDtos(
                offset, pageSize, toPdf,
                exchangeCurrency, principal.getId(), visitFilterOptions
        );

        return ResponseEntity.ok(visitsPage);
    }

    @GetMapping("/visits/{visitId}")
    public ResponseEntity<VisitOutDto> findSingleVisit(@PathVariable long visitId) {
        Visit visit = visitService.findById(visitId);

        return ResponseEntity.ok(visitMapper.toDto(visit));
    }

    @PostMapping("/visits")
    public ResponseEntity<?> createVisit( ){
        return null;
    }




    private static VisitFilterOptions getVisitFilterOptions(
            Long customerId, String customerName, Long clerkId, String clerkName,
            TimeOperator scheduleCondition, LocalDate scheduleDateFrom, LocalDate scheduleDateTo,
            String brandName, String vehicleVin, String vehicleRegistry,
            TimeOperator bookedCondition, LocalDateTime bookedOn,
            String sortBy, String sortOrder) {
        return new VisitFilterOptions(customerId, customerName,
                clerkId, clerkName, scheduleCondition, scheduleDateFrom, scheduleDateTo,
                brandName, vehicleVin, vehicleRegistry, bookedCondition, bookedOn, sortBy, sortOrder);
    }

    private Page<VisitOutDto> getVisitOutDtos(int offset, int pageSize, boolean toPdf,
                                              String exchangeCurrency, long userId,
                                              VisitFilterOptions visitFilterOptions) throws IOException, DocumentException {
        Pageable pageable = PageRequest.of(offset, pageSize);

        List<Visit> visits = visitService.findAll(visitFilterOptions);

        List<VisitOutDto> visitOutDtos = visits.stream().map(visitMapper::toDto).toList();

        visitOutDtos = visitService.calculateCost(visitOutDtos, exchangeCurrency);

        UserEntity user = userService.getById(userId);

        if (toPdf) {
            ByteArrayOutputStream pdf = visitService.createPdf(visitOutDtos, user);
            eventPublisher.publishEvent(new EmailReportEvent(pdf, user));
        }

        return new PageImpl<>(visitOutDtos, pageable, visitOutDtos.size());
    }

}
