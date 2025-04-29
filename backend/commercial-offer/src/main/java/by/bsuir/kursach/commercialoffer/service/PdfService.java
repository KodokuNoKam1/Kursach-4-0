package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.model.Offer;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateOfferPdf(Offer offer) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Заголовок
        Paragraph title = new Paragraph("Commercial Offer")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);

        // Таблица с данными
        Table table = new Table(new float[]{150, 300});
        table.addHeaderCell(new Paragraph("Field").setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Paragraph("Value").setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell("Title");
        table.addCell(offer.getTitle());
        table.addCell("Description");
        table.addCell(offer.getDescription() != null ? offer.getDescription() : "");
        table.addCell("Amount");
        table.addCell(String.format("%.2f %s", offer.getAmount(), offer.getCurrency().getCode()));
        document.add(table);

        document.close();
        return baos.toByteArray();
    }
}