package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.model.Offer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] exportOffersToExcel(List<Offer> offers) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Offers");

        // Заголовок
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Title");
        header.createCell(2).setCellValue("Description");
        header.createCell(3).setCellValue("Amount");
        header.createCell(4).setCellValue("Currency");

        // Данные
        int rowNum = 1;
        for (Offer offer : offers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(offer.getId());
            row.createCell(1).setCellValue(offer.getTitle());
            row.createCell(2).setCellValue(offer.getDescription() != null ? offer.getDescription() : "");
            row.createCell(3).setCellValue(offer.getAmount());
            row.createCell(4).setCellValue(offer.getCurrency().getCode());
        }

        // Автоподбор ширины столбцов
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        // Запись в поток
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }
}