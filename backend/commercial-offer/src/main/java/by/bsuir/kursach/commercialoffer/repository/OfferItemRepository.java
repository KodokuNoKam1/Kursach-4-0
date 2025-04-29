package by.bsuir.kursach.commercialoffer.repository;

import by.bsuir.kursach.commercialoffer.model.OfferItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferItemRepository extends JpaRepository<OfferItem, Long> {
}