package org.example.smartgarage.services.contracts;

import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.models.enums.Status;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface VisitService {
    List<Visit> findAll(VisitFilterOptions visitFilterOptions);

    Visit findById(long visitId);

    List<VisitOutDto> calculateCost(List<VisitOutDto> visitOutDtos, String exchangeCurrency) throws IOException;

    ByteArrayOutputStream createPdf(List<VisitOutDto> visitOutDtos, UserEntity principal) throws IOException;

    Visit create(Visit visit, Long clerkId);

    Visit updateStatus(Status status, long visitId);

    void deleteVisit(long visitId);
}
