package by.bsuir.kursach.commercialoffer.util;

import by.bsuir.kursach.commercialoffer.model.Offer;

public class HtmlOfferDocumentFactory implements OfferDocumentFactory {
    @Override
    public String createDocument(Offer offer) {
        return "<html><body>HTML document for offer " + offer.getId() + "</body></html>";
    }
}