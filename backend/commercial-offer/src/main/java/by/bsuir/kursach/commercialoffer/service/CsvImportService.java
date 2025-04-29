package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.entity.Currency;
import by.bsuir.kursach.commercialoffer.entity.Offer;
import by.bsuir.kursach.commercialoffer.repository.CurrencyRepository;
import by.bsuir.kursach.commercialoffer.repository.OfferRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    private final OfferRepository offerRepository;
    private final CurrencyRepository currencyRepository;
    private final OfferHistoryService historyService;

    public CsvImportService(OfferRepository offerRepository, CurrencyRepository currencyRepository, OfferHistoryService historyService) {
        this.offerRepository = offerRepository;
        this.currencyRepository = currencyRepository;
        this.historyService = historyService;
    }

    public List<Offer> importOffersFromCsv(MultipartFile file) throws IOException, CsvValidationException {
        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB limit
            throw new IllegalArgumentException("File size exceeds 10 MB limit");
        }
        List<Offer> offers = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = csvReader.readNext();
            if (header == null || header.length != 4 || !header[0].equals("title") || !header[1].equals("description") || !header[2].equals("amount") || !header[3].equals("currencyId")) {
                throw new IllegalArgumentException("Invalid CSV format. Expected headers: title,description,amount,currencyId");
            }
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                if (record.length != 4) {
                    throw new IllegalArgumentException("Invalid CSV row: " + String.join(",", record));
                }
                Offer offer = new Offer();
                try {
                    offer.setTitle(record[0]);
                    offer.setDescription(record[1]);
                    offer.setAmount(Double.parseDouble(record[2]));
                    Currency currency = currencyRepository.findById(Long.parseLong(record[3]))
                            .orElseThrow(() -> new IllegalArgumentException("Invalid currency ID: " + record[3]));
                    offer.setCurrency(currency);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format in row: " + String.join(",", record));
                }
                Offer savedOffer = offerRepository.save(offer);
                historyService.logAction(savedOffer, "IMPORTED", "Offer imported from CSV");
                offers.add(savedOffer);
            }
        }
        return offers;
    }
}