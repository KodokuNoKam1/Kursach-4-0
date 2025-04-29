package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.model.Offer;
import by.bsuir.kursach.commercialoffer.model.OfferHistory;
import by.bsuir.kursach.commercialoffer.repository.OfferHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OfferHistoryService {

    private final OfferHistoryRepository historyRepository;

    public OfferHistoryService(OfferHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void logAction(Offer offer, String action, String details) {
        OfferHistory history = new OfferHistory();
        history.setOffer(offer);
        history.setAction(action);
        history.setDetails(details);
        history.setTimestamp(LocalDateTime.now());
        historyRepository.save(history);
    }
}