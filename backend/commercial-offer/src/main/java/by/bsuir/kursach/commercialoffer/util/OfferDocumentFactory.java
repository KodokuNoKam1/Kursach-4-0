package by.bsuir.kursach.commercialoffer.util;

import by.bsuir.kursach.commercialoffer.model.Offer;

public interface OfferDocumentFactory {
    String createDocument(Offer offer);
}