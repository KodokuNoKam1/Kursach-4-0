package by.bsuir.kursach.commercialoffer.util;

import java.util.ArrayList;
import java.util.List;

public class OfferStatusObservable {
    private final List<OfferStatusObserver> observers = new ArrayList<>();

    public void addObserver(OfferStatusObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String offerId, String status) {
        for (OfferStatusObserver observer : observers) {
            observer.update(offerId, status);
        }
    }
}