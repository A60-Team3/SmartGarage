package org.example.smartgarage.services.contracts;

import com.itextpdf.text.DocumentException;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.models.UserEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface ReportService {
    ByteArrayOutputStream createPdf(List<VisitOutDto> visits, UserEntity user) throws IOException, DocumentException;
}
