package by.bsuir.kursach.commercialoffer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import by.bsuir.kursach.commercialoffer.model.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}