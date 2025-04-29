package by.bsuir.kursach.commercialoffer.util;

public class EmailNotificationObserver implements OfferStatusObserver {
    @Override
    public void update(String offerId, String status) {
        System.out.println("Email sent: Offer " + offerId + " status changed to " + status);
    }
}