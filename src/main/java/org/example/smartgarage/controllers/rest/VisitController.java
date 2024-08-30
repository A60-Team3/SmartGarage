package org.example.smartgarage.controllers.rest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.smartgarage.dtos.VisitOutDto;
import org.example.smartgarage.mappers.VisitMapper;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.ReportService;
import org.example.smartgarage.services.contracts.VisitService;
import org.example.smartgarage.utils.filtering.TimeOperator;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/garage")
public class VisitController {
    private final VisitService visitService;
    private final ReportService reportService;
    private final VisitMapper visitMapper;

    public VisitController(VisitService visitService, ReportService reportService, VisitMapper visitMapper) {
        this.visitService = visitService;
        this.reportService = reportService;
        this.visitMapper = visitMapper;
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
                                                     @RequestParam String exchangeCurrency,
                                                     @AuthenticationPrincipal CustomUserDetails principal) throws IOException, DocumentException {
        VisitFilterOptions visitFilterOptions = getVisitFilterOptions(
                null, customerName, null, clerkName,
                scheduleCondition, scheduleDateFrom, scheduleDateTo,
                brandName, vehicleVin, vehicleRegistry,
                bookedCondition, bookedOn, sortBy, sortOrder
        );

        Pageable pageable = PageRequest.of(offset, pageSize);

        List<Visit> visits = visitService.findAll(visitFilterOptions);

        List<VisitOutDto> visitOutDtos = visits.stream().map(visitMapper::toDto).toList();

        visitOutDtos = visitService.calculateCost(visitOutDtos, exchangeCurrency);

        if (toPdf) {
            Document document = reportService.createPdf(visitOutDtos, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(document);
        }

        Page<VisitOutDto> visitsPage = new PageImpl<>(visitOutDtos, pageable, visitOutDtos.size());

        return ResponseEntity.ok(visitsPage);
    }

    @PreAuthorize("hasRole('CUSTOMER') && #userId == principal.getId()")
    @GetMapping("/users/{userId}/visits")
    public ResponseEntity<List<VisitOutDto>> findAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
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
                                                     @RequestParam String exchangeCurrency,
                                                     @AuthenticationPrincipal CustomUserDetails principal) throws IOException {
        VisitFilterOptions visitFilterOptions = getVisitFilterOptions(
                userId, customerName, null, clerkName,
                scheduleCondition, scheduleDateFrom, scheduleDateTo,
                brandName, vehicleVin, vehicleRegistry,
                bookedCondition, bookedOn, sortBy, sortOrder
        );

        Pageable pageable = PageRequest.of(offset, pageSize);

        List<Visit> visits = visitService.findAll(visitFilterOptions);

        Page<Visit> visitsPage = new PageImpl<>(visits, pageable, visits.size());
        List<VisitOutDto> visitOutDtos = visitsPage.map(visitMapper::toDto).toList();

        return ResponseEntity.ok(visitOutDtos);
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/clerks/visits")
    public ResponseEntity<List<VisitOutDto>> findAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
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
                                                     @RequestParam String exchangeCurrency,
                                                     @AuthenticationPrincipal CustomUserDetails user) throws IOException {
        VisitFilterOptions visitFilterOptions = getVisitFilterOptions(
                customerId, customerName, clerkId, clerkName,
                scheduleCondition, scheduleDateFrom, scheduleDateTo,
                brandName, vehicleVin, vehicleRegistry,
                bookedCondition, bookedOn, sortBy, sortOrder
        );

        Pageable pageable = PageRequest.of(offset, pageSize);

        List<Visit> visits = visitService.findAll(visitFilterOptions);


        Page<Visit> visitsPage = new PageImpl<>(visits, pageable, visits.size());
        List<VisitOutDto> visitOutDtos = visitsPage.map(visitMapper::toDto).toList();

        return ResponseEntity.ok(visitOutDtos);
    }

    @GetMapping("/visits/{visitId}")
    public ResponseEntity<VisitOutDto> findSingleVisit(@PathVariable long visitId) {
        Visit visit = visitService.findById(visitId);

        return ResponseEntity.ok(visitMapper.toDto(visit));
    }

    private static VisitFilterOptions getVisitFilterOptions(Long customerId, String customerName, Long clerkId, String clerkName, TimeOperator scheduleCondition, LocalDate scheduleDateFrom, LocalDate scheduleDateTo, String brandName, String vehicleVin, String vehicleRegistry, TimeOperator bookedCondition, LocalDateTime bookedOn, String sortBy, String sortOrder) {
        return new VisitFilterOptions(customerId, customerName,
                clerkId, clerkName, scheduleCondition, scheduleDateFrom, scheduleDateTo,
                brandName, vehicleVin, vehicleRegistry, bookedCondition, bookedOn, sortBy, sortOrder);
    }
}
