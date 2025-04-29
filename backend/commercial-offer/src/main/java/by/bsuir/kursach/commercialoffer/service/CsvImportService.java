package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.model.Currency;
import by.bsuir.kursach.commercialoffer.model.Offer;
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
        List<Offer> offers = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            csvReader.readNext(); // Пропустить заголовок
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                Offer offer = new Offer();
                offer.setTitle(record[0]);
                offer.setDescription(record[1]);
                offer.setAmount(Double.parseDouble(record[2]));
                Currency currency = currencyRepository.findById(Long.parseLong(record[3]))
                        .orElseThrow(() -> new IllegalArgumentException("Invalid currency ID: " + record[3]));
                offer.setCurrency(currency);
                Offer savedOffer = offerRepository.save(offer);
                historyService.logAction(savedOffer, "IMPORTED", "Offer imported from CSV");
                offers.add(savedOffer);
            }
        }
        return offers;
    }
}