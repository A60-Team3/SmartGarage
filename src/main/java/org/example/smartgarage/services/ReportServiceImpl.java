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
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.CurrencyCode;
import org.example.smartgarage.services.contracts.ReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final static String REPORT = "A60 Smart Garage Report";
    private final static String QUOTE = "A60 Smart Garage Quote";
    private final static String REPORT_SUBJECT = "Your visits";
    private final static String QUOTE_SUBJECT = "Your personal quotation";

    @Value("${pdf.encryption.master.password}")
    private String MASTER_PASSWORD;
    private static final String GARAGE_LOGO = "src/main/resources/static/img/garage_logo.jpg";

    @Override
    public byte[] createVisitReport(List<VisitOutDto> visits, UserEntity user) throws IOException {
        if (visits.isEmpty()) return new ByteArrayOutputStream().toByteArray();

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
        pdfDocument.getDocumentInfo().setTitle(REPORT);
        pdfDocument.getDocumentInfo().setSubject(REPORT_SUBJECT);

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
            document.add(new Paragraph());

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
                                .add(service.toString())
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
                            .add(new Text(
                                    visit
                                            .getTotalCost()
                                            .setScale(2, RoundingMode.HALF_UP).toString())
                                    .setFontColor(ColorConstants.RED))
                            .add(" ")
                            .add(visit.getCurrency())
                            .add(new Tab())
                            .add(new Tab())
                            .add(new Text(String.format("Conversion Rate: %.4f", visit.getExchangeRate())))
            );

            document.add(new Paragraph());
            document.add(new LineSeparator(new DashedLine()));

            if (i != visits.size() - 1) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }

        document.close();
        pdfWriter.close();
        byte[] pdf = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return pdf;
    }

    @Override
    public ByteArrayOutputStream createQuotation(UserEntity client, LocalDate desiredDate,
                                                 CurrencyCode desiredCurrency, String carBrand,
                                                 String carModel, Integer carYear,
                                                 List<ServiceType> services, BigDecimal totalCost, double exchangeRate) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(
                byteArrayOutputStream,
                new WriterProperties()
                        .setStandardEncryption(
                                client.getLastName().getBytes(),
                                MASTER_PASSWORD.getBytes(),
                                EncryptionConstants.ALLOW_PRINTING,
                                EncryptionConstants.ENCRYPTION_AES_256)
        );

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        pdfDocument.getDocumentInfo().setAuthor(String.format("%s %s", client.getFirstName(), client.getLastName()));
        pdfDocument.getDocumentInfo().setTitle(REPORT);
        pdfDocument.getDocumentInfo().setSubject(REPORT_SUBJECT);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        Image logo = new Image(ImageDataFactory.create(GARAGE_LOGO));

        Document document = new Document(pdfDocument, PageSize.A4);
        document.setTextAlignment(TextAlignment.JUSTIFIED);
        document.setFont(font);

        Paragraph header = new Paragraph()
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setPadding(12f)
                .add(new Tab())
                .add(new Tab())
                .add(new Text("Smart Garage Inc").setBold()
                        .setBackgroundColor(ColorConstants.BLACK)
                        .setFontColor(ColorConstants.YELLOW)
                        .setFontSize(36f))
                .add(logo.scale(0.20f, 0.20f));
        document.add(header);

        document.add(new LineSeparator(new DashedLine()));
        document.add(new Paragraph());

        Paragraph booked = new Paragraph()
                .add(new Tab())
                .add(new Text("Visit possible on: ")
                        .setBold())
                .add(new Text(desiredDate.toString())
                        .setFontColor(ColorConstants.RED)
                        .setItalic());
        document.add(booked);

        Paragraph customer = new Paragraph()
                .add(new Tab())
                .add(new Text("Appointment requested by: ").setBold())
                .add(new Text(String.format("%s %s", client.getFirstName(), client.getLastName())).setItalic());
        document.add(customer);


        if (carBrand == null) {
            Paragraph firstLine = new Paragraph()
                    .add(new Tab())
                    .add(new Text("The car that you have specified is not in our list of supported cars.")
                            .setFontColor(ColorConstants.RED)
                            .setItalic());
            Paragraph secondLine = new Paragraph()
                    .add(new Tab())
                    .add(new Text("Below you will find our base prices for the services you requested.")
                            .setFontColor(ColorConstants.RED)
                            .setItalic());
            Paragraph thirdLine = new Paragraph()
                    .add(new Tab())
                    .add(new Text("Final price will be calculated on spot depending on the car condition.")
                            .setFontColor(ColorConstants.RED)
                            .setItalic());

            document.add(firstLine).add(secondLine).add(thirdLine);
        } else {
            createVehicleParagraph(carBrand, carModel, carYear, document);
        }

        document.add(
                new Paragraph()
                        .add(new Tab())
                        .add(new Text("Services requested:").setBold())
        );

        services.forEach(service -> {
            document.add(
                    new Paragraph()
                            .add(new Tab())
                            .add(new Tab())
                            .add(service.toString())
            );
        });

        document.add(
                new Paragraph()
                        .add(new Tab())
                        .add("Visit costs total: ").setBold()
                        .add(new Text(totalCost.setScale(2, RoundingMode.HALF_UP).toString()).setFontColor(ColorConstants.RED))
                        .add(" ")
                        .add(desiredCurrency.getDescription())
                        .add(new Tab())
                        .add(new Tab())
                        .add(new Text(String.format("Conversion Rate: %.4f", exchangeRate)))
        );

        document.add(new Paragraph());
        document.add(new LineSeparator(new DashedLine()));

        document.close();

        return byteArrayOutputStream;
    }

    private void createVehicleParagraph(String carBrand, String carModel, int carYear, Document document) {
        Paragraph vehicleBrandTitle = new Paragraph().add(new Tab()).add(new Text("Brand: ").setBold());
        Text vehicleBrand = new Text(carBrand);
        vehicleBrandTitle.add(vehicleBrand);

        Paragraph vehicleModelTitle = new Paragraph()
                .add(new Tab()).add(new Tab())
                .add(new Text("Model: ").setBold());
        Text vehicleModel = new Text(carModel);
        vehicleModelTitle.add(vehicleModel);


        Paragraph vehicleYearTitle = new Paragraph()
                .add(new Tab()).add(new Tab()).add(new Tab())
                .add(new Text("Produced: ").setBold());
        Text vehicleYear = new Text(String.valueOf(carYear));
        vehicleYearTitle.add(vehicleYear);

        document.add(vehicleBrandTitle). add(vehicleModelTitle).add(vehicleYearTitle);
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
