package org.example.smartgarage.controllers.rest;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.VisitInDto;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.events.EmailReportEvent;
import org.example.smartgarage.mappers.VisitMapper;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.models.enums.CurrencyCode;
import org.example.smartgarage.models.enums.Status;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                                     @Parameter(description = "Vehicle id")
                                     @RequestParam(required = false) Long vehicleId,
                                     @Parameter(description = "List of order ids")
                                     @RequestParam(required = false) List<Long> orders,
                                     @RequestParam(required = false) TimeOperator scheduleCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateFrom,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateTo,
                                     @RequestParam(required = false) TimeOperator bookedCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd")
                                     @RequestParam(required = false) LocalDate bookedOn,
                                     @Parameter(description = "Options - all field names")
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) String sortOrder,
                                     @RequestParam(required = false) boolean toPdf,
                                     @RequestParam(required = false) CurrencyCode exchangeCurrency,
                                     @AuthenticationPrincipal CustomUserDetails principal) {
        VisitFilterOptions visitFilterOptions = createVisitFilterOptions(
                null, customerName, null, clerkName,
                vehicleId, orders, scheduleCondition, scheduleDateFrom, scheduleDateTo,
                bookedCondition, bookedOn, sortBy, sortOrder
        );

        Page<VisitOutDto> visitsPage = createVisitOutDtos(
                offset, pageSize, toPdf,
                exchangeCurrency, principal.getId(), visitFilterOptions
        );

        return ResponseEntity.ok(visitsPage);
    }

    @PreAuthorize("!hasRole('MECHANIC') or #userId == principal.getId()")
    @GetMapping("/users/{userId}/visits")
    public ResponseEntity<?> findAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                     @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                     @Parameter(description = "Logged customer id")
                                     @PathVariable(required = false) Long userId,
                                     @Parameter(description = "Partial or full first name")
                                     @RequestParam(required = false) String customerName,
                                     @Parameter(description = "Partial or full last name")
                                     @RequestParam(required = false) String clerkName,
                                     @Parameter(description = "Vehicle id")
                                     @RequestParam(required = false) Long vehicleId,
                                     @Parameter(description = "List of order ids")
                                     @RequestParam(required = false) List<Long> orders,
                                     @RequestParam(required = false) TimeOperator scheduleCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateFrom,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateTo,
                                     @RequestParam(required = false) TimeOperator bookedCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd")
                                     @RequestParam(required = false) LocalDate bookedOn,
                                     @Parameter(description = "Options - all field names")
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) String sortOrder,
                                     @RequestParam(required = false) boolean toPdf,
                                     @RequestParam(required = false) CurrencyCode exchangeCurrency,
                                     @AuthenticationPrincipal CustomUserDetails principal) {
        VisitFilterOptions visitFilterOptions = createVisitFilterOptions(
                userId, customerName, null, clerkName,
                vehicleId, orders, scheduleCondition, scheduleDateFrom, scheduleDateTo,
                bookedCondition, bookedOn, sortBy, sortOrder
        );

        Page<VisitOutDto> visitsPage = createVisitOutDtos(
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
                                     @Parameter(description = "Vehicle id")
                                     @RequestParam(required = false) Long vehicleId,
                                     @Parameter(description = "List of order ids")
                                     @RequestParam(required = false) List<Long> orders,
                                     @RequestParam(required = false) TimeOperator scheduleCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateFrom,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     @RequestParam(required = false) LocalDate scheduleDateTo,
                                     @RequestParam(required = false) TimeOperator bookedCondition,
                                     @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd")
                                     @RequestParam(required = false) LocalDate bookedOn,
                                     @Parameter(description = "Options - all field names")
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) String sortOrder,
                                     @RequestParam(required = false) boolean toPdf,
                                     @RequestParam(required = false) CurrencyCode exchangeCurrency,
                                     @AuthenticationPrincipal CustomUserDetails principal) {
        VisitFilterOptions visitFilterOptions = createVisitFilterOptions(
                customerId, customerName, clerkId, clerkName, vehicleId, orders,
                scheduleCondition, scheduleDateFrom, scheduleDateTo,
                bookedCondition, bookedOn, sortBy, sortOrder
        );

        Page<VisitOutDto> visitsPage = createVisitOutDtos(
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

    @PreAuthorize("hasRole('CLERK')")
    @PostMapping("/visits")
    public ResponseEntity<?> createVisit(@Valid VisitInDto dto,
                                         @AuthenticationPrincipal CustomUserDetails loggedClerk) {
        Visit visit = visitMapper.toEntity(dto);
        Visit savedVisit = visitService.create(visit, loggedClerk.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(visitMapper.toDto(savedVisit));
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC', 'HR')")
    @PutMapping("/visits/{visitId}")
    public ResponseEntity<?> updateVisit(@RequestParam(required = false) Status status,
                                         @RequestParam(required = false) LocalDate bookedDate,
                                         @PathVariable long visitId) {

        Visit updatedVisit = visitService.updateVisit(status, visitId, bookedDate);

        return ResponseEntity.ok(visitMapper.toDto(updatedVisit));
    }

    @PreAuthorize("hasRole('CLERK')")
    @DeleteMapping("/visits/{visitId}")
    public ResponseEntity<?> deleteVisit(@PathVariable long visitId) {

        visitService.deleteVisit(visitId);

        return ResponseEntity.ok("Visit successfully deleted");
    }

    @GetMapping("/findPageForVisit")
    public ResponseEntity<Integer> getPageForVisit(
            @RequestParam("visitId") Long visitId,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            VisitFilterOptions filterOptions) {

        // Use your service to get all visits with current filtering and sorting
        List<Visit> allFilteredVisits = visitService.findAll(filterOptions);

        // Find the index of the updated visit
        int visitIndex = -1;
        for (int i = 0; i < allFilteredVisits.size(); i++) {
            if (allFilteredVisits.get(i).getId() == visitId) {
                visitIndex = i;
                break;
            }
        }

        // Calculate the page number
        int pageNumber = visitIndex / pageSize;

        return ResponseEntity.ok(pageNumber);
    }

    @GetMapping("/visits/report")
    public ResponseEntity<byte[]> generateReport(@RequestParam List<Long> visitIds,
                                 @RequestParam String email,
                                 @RequestParam(required = false) CurrencyCode exchangeCurrency){

        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide valid email");
        }

        List<Visit> visitsToReport = visitService.findAllById(visitIds);
        List<VisitOutDto> visitOutDtos = visitsToReport.stream().map(visitMapper::toDto).toList();
        visitOutDtos = visitService.calculateCost(visitOutDtos,exchangeCurrency);

        byte[] pdf = visitService.createPdf(visitOutDtos, user);

        eventPublisher.publishEvent(new EmailReportEvent(pdf, user));

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", String.format("Visit_Report_%s.pdf", currentTime));
        headers.setContentLength(pdf.length);

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    private static VisitFilterOptions createVisitFilterOptions(
            Long customerId, String customerName, Long clerkId, String clerkName,
            Long vehicleId, List<Long> orders,
            TimeOperator scheduleCondition, LocalDate scheduleDateFrom, LocalDate scheduleDateTo,
            TimeOperator bookedCondition, LocalDate bookedOn,
            String sortBy, String sortOrder) {
        return new VisitFilterOptions(customerId, customerName,
                clerkId, clerkName, vehicleId, orders,
                scheduleCondition, scheduleDateFrom, scheduleDateTo,
                bookedCondition, bookedOn, sortBy, sortOrder);
    }

    private Page<VisitOutDto> createVisitOutDtos(int offset, int pageSize, boolean toPdf,
                                                 CurrencyCode exchangeCurrency, long userId,
                                                 VisitFilterOptions visitFilterOptions){
        Pageable pageable = PageRequest.of(offset, pageSize);

        List<Visit> visits = visitService.findAll(visitFilterOptions);
        List<VisitOutDto> visitOutDtos = visits.stream().map(visitMapper::toDto).toList();

        if (!visitOutDtos.isEmpty()) {
            visitOutDtos = visitService.calculateCost(visitOutDtos, exchangeCurrency);

            UserEntity user = userService.getById(userId);

            if (toPdf) {
                byte[] pdf = visitService.createPdf(visitOutDtos, user);
                eventPublisher.publishEvent(new EmailReportEvent(pdf, user));
            }
        }

        return new PageImpl<>(visitOutDtos, pageable, visitOutDtos.size());
    }

}
