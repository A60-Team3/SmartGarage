package org.example.smartgarage.services.contracts;

import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.models.enums.CurrencyCode;
import org.example.smartgarage.models.enums.Status;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.springframework.data.domain.Page;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

public interface VisitService {
    List<Visit> findAll(VisitFilterOptions visitFilterOptions);

    Page<Visit> findAll(VisitFilterOptions visitFilterOptions, int pageIndex, int pageSize);

    Visit findById(long visitId);

    List<VisitOutDto> calculateCost(List<VisitOutDto> visitOutDtos, CurrencyCode exchangeCurrency);

    ByteArrayOutputStream createPdf(List<VisitOutDto> visitOutDtos, UserEntity principal);

    Visit create(Visit visit, Long clerkId);

    Visit updateVisit(Status status, long visitId, LocalDate bookedDate);

    void deleteVisit(long visitId);

    int calculateVisitPage(long visitId, VisitFilterOptions filterOptions, int pageSize);
}
