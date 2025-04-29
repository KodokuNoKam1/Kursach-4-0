package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.model.Offer;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateOfferPdf(Offer offer) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Commercial Offer"));
        document.add(new Paragraph("Title: " + offer.getTitle()));
        document.add(new Paragraph("Description: " + offer.getDescription()));
        document.add(new Paragraph("Amount: " + offer.getAmount() + " " + offer.getCurrency().getCode()));

        document.close();
        return baos.toByteArray();
    }
}