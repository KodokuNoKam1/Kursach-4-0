package by.bsuir.kursach.commercialoffer.util;

public interface OfferStatusObserver {
    void update(String offerId, String status);
}