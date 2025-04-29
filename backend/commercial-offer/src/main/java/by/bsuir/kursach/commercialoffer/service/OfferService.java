package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.model.Offer;
import by.bsuir.kursach.commercialoffer.model.OfferItem;
import by.bsuir.kursach.commercialoffer.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Offer createOffer(Offer offer) {
        offer.setCreatedDate(LocalDateTime.now());
        offer.setStatus("DRAFT");
        double totalPrice = offer.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        offer.setTotalPrice(totalPrice);
        return offerRepository.save(offer);
    }
}