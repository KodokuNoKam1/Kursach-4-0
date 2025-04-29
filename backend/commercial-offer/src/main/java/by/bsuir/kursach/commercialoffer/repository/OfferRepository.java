package by.bsuir.kursach.commercialoffer.repository;

import by.bsuir.kursach.commercialoffer.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}