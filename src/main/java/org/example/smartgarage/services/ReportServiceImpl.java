package org.example.smartgarage.services;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.services.contracts.ReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final static String TITLE = "A60 Smart Garage Report";
    private final static String SUBJECT = "Your visits";

    @Value("${pdf.encryption.master.password}")
    private String MASTER_PASSWORD;
    private static final String GARAGE_LOGO = "src/main/resources/static/img/garage_logo.jpg";

    @Override
    public ByteArrayOutputStream createPdf(List<VisitOutDto> visits, UserEntity user) throws IOException {
        if (visits.isEmpty()) return new ByteArrayOutputStream();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(
                byteArrayOutputStream,
                new WriterProperties()
                        .setStandardEncryption(
                                user.getPhoneNumber().getBytes(),
                                MASTER_PASSWORD.getBytes(),
                                EncryptionConstants.ALLOW_PRINTING,
                                EncryptionConstants.ENCRYPTION_AES_256)
        );

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        pdfDocument.getDocumentInfo().setAuthor(String.format("%s %s", user.getFirstName(), user.getLastName()));
        pdfDocument.getDocumentInfo().setTitle(TITLE);
        pdfDocument.getDocumentInfo().setSubject(SUBJECT);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        Image logo = new Image(ImageDataFactory.create(GARAGE_LOGO));

        Document document = new Document(pdfDocument, PageSize.A4);
        document.setTextAlignment(TextAlignment.JUSTIFIED);
        document.setFont(font);

        for (int i = 0; i < visits.size(); i++) {
            VisitOutDto visit = visits.get(i);

            Paragraph header = new Paragraph()
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setPadding(12f)
                    .add(new Tab())
                    .add(new Tab())
                    .add(new Text("Smart Garage Inc").setBold()
                            .setBackgroundColor(ColorConstants.BLACK)
                            .setFontColor(ColorConstants.YELLOW)
                            .setFontSize(36f))
                    .add(logo.scale(0.25f, 0.25f));
            document.add(header);

            document.add(new LineSeparator(new DashedLine()));

            Paragraph booked = new Paragraph()
                    .add(new Tab())
                    .add(new Text("Visit scheduled on: ")
                            .setBold())
                    .add(new Text(visit.getBookedDate())
                            .setFontColor(ColorConstants.RED)
                            .setItalic());
            document.add(booked);

            Paragraph status = new Paragraph()
                    .add(new Tab())
                    .add(new Text("Visit current status: ")
                            .setBold())
                    .add(new Text(visit.getStatus())
                            .setFontColor(ColorConstants.RED)
                            .setItalic());
            document.add(status);

            Paragraph customer = new Paragraph()
                    .add(new Tab())
                    .add(new Text("Visitor: ").setBold())
                    .add(new Text(visit.getClientName()).setItalic());
            document.add(customer);

            Paragraph clerk = new Paragraph()
                    .add(new Tab())
                    .add(new Text("Served by: ").setBold())
                    .add(new Text(visit.getEmployeeName()).setItalic());
            document.add(clerk);

            Paragraph vehicle = createVehicleParagraph(visit);
            document.add(vehicle);

            document.add(
                    new Paragraph()
                            .add(new Tab())
                            .add(new Text("Services carried out:").setBold())
            );

            visit.getServices().forEach(service -> {
                document.add(
                        new Paragraph()
                                .add(new Tab())
                                .add(new Tab())
                                .add(service)
                );
            });

            document.add(
                    new Paragraph()
                            .add(new Tab())
                            .add("Visit history:").setBold()
            );

            visit.getHistory().forEach(event -> {
                document.add(
                        new Paragraph()
                                .add(new Tab())
                                .add(new Tab())
                                .add(event)
                );
            });

            document.add(
                    new Paragraph()
                            .add(new Tab())
                            .add("Visit costs total: ").setBold()
                            .add(new Text(visit.getTotalCost().toString()).setFontColor(ColorConstants.RED))
                            .add(" ")
                            .add(visit.getCurrency())
                            .add(new Tab())
                            .add(new Tab())
                            .add(new Text(String.format("Conversion Rate: %.4f", visit.getExchangeRate())))
            );

            document.add(new LineSeparator(new DashedLine()));

            if (i != visits.size() - 1) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }

        document.close();

        return byteArrayOutputStream;
    }

    private static Paragraph createVehicleParagraph(VisitOutDto visit) {
        Text vehicleBrandTitle = new Text("Brand: ").setBold();
        Text vehicleBrand = new Text(visit.getVehicle().brandName() + " ");
        Text vehicleModelTitle = new Text("Model: ").setBold();
        Text vehicleModel = new Text(visit.getVehicle().modelName() + " ");
        Text vehicleYearTitle = new Text("Produced: ").setBold();
        Text vehicleYear = new Text(visit.getVehicle().year() + " ");
        Text vehicleLicenseTitle = new Text("License plate: ").setBold();
        Text vehicleLicense = new Text(visit.getVehicle().licensePlate() + " ");
        Text vehicleVinTitle = new Text("VIN: ").setBold();
        Text vehicleVin = new Text(visit.getVehicle().vin());

        return new Paragraph()
                .add(new Tab())
                .add(vehicleBrandTitle)
                .add(vehicleBrand)
                .add(vehicleModelTitle)
                .add(vehicleModel)
                .add(vehicleYearTitle)
                .add(vehicleYear)
                .add(vehicleLicenseTitle)
                .add(vehicleLicense)
                .add(vehicleVinTitle)
                .add(vehicleVin);
    }
}
