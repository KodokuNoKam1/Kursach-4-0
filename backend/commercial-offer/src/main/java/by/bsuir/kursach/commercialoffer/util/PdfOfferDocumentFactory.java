package by.bsuir.kursach.commercialoffer.util;

import by.bsuir.kursach.commercialoffer.model.Offer;

public class PdfOfferDocumentFactory implements OfferDocumentFactory {
    @Override
    public String createDocument(Offer offer) {
        return "PDF document for offer " + offer.getId();
    }
}