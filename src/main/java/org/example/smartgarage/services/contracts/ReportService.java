package org.example.smartgarage.services.contracts;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import org.example.smartgarage.dtos.VisitOutDto;
import org.example.smartgarage.security.CustomUserDetails;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    Document createPdf(List<VisitOutDto> visits, CustomUserDetails user) throws IOException, DocumentException;
}
