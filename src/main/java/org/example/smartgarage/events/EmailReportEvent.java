package org.example.smartgarage.events;

import org.example.smartgarage.models.UserEntity;

import java.io.ByteArrayOutputStream;

public record EmailReportEvent (ByteArrayOutputStream pdfDocument,
                                UserEntity user) {
}
