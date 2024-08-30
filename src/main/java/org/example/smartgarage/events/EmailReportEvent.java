package org.example.smartgarage.events;

import com.itextpdf.text.Document;
import org.example.smartgarage.models.UserEntity;

public record EmailReportEvent (Document pdfDocument,
                                UserEntity user) {
}
