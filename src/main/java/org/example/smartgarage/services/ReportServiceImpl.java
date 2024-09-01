package org.example.smartgarage.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.services.contracts.ReportService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final static String TITLE = "A60 Smart Garage Report";
    private final static String SUBJECT = "Your visits";

    @Override
    public ByteArrayOutputStream createPdf(List<VisitOutDto> visits, UserEntity user) throws DocumentException {
        if (visits.isEmpty()) return new ByteArrayOutputStream();

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.addTitle(TITLE);
        document.addSubject(SUBJECT);
        document.addAuthor(user.getUsername());
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14f, Font.NORMAL, BaseColor.BLACK);
        document.open();

        for (VisitOutDto visit : visits) {

            Paragraph booked = new Paragraph(String.format("Visit scheduled on: %s", visit.getBookedDate()), font);
            booked.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(booked);

            Paragraph customer = new Paragraph(String.format("Visitor: %s", visit.getClientName()), font);
            customer.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(customer);

            Paragraph clerk = new Paragraph(String.format("Served by: %s", visit.getEmployeeName()), font);
            clerk.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(clerk);

            Paragraph vehicle = createVehicleParagraph(visit, font);
            vehicle.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(vehicle);

            visit.getServices().forEach(s -> {
                Paragraph service = new Paragraph(s, font);
                service.setAlignment(Element.ALIGN_JUSTIFIED);
                try {
                    document.add(service);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            });

            visit.getHistory().forEach(event -> {
                Paragraph service = new Paragraph(event, font);
                service.setAlignment(Element.ALIGN_JUSTIFIED);
                try {
                    document.add(service);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            });

            Chunk totalCost = new Chunk(visit.getTotalCost().toString());
            Chunk currency = new Chunk(visit.getCurrency());
            Paragraph cost = new Paragraph(String.format("Visit costs total: %s %s", totalCost, currency), font);
            cost.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(cost);
        }

        document.close();

        return byteArrayOutputStream;
    }

    private static Paragraph createVehicleParagraph(VisitOutDto visit, Font font) {
        Phrase vehicleBrand = new Phrase(visit.getVehicle().brandName(), font);
        Phrase vehicleModel = new Phrase(visit.getVehicle().modelName(), font);
        Phrase vehicleYear = new Phrase(String.valueOf(visit.getVehicle().year()), font);
        Phrase vehicleLicense = new Phrase(String.valueOf(visit.getVehicle().licensePlate()), font);
        Phrase vehicleVin = new Phrase(String.valueOf(visit.getVehicle().vin()), font);

        Paragraph vehicle = new Paragraph();
        vehicle.add(vehicleBrand);
        vehicle.add(vehicleModel);
        vehicle.add(vehicleYear);
        vehicle.add(vehicleLicense);
        vehicle.add(vehicleVin);
        return vehicle;
    }
}
