package by.bsuir.kursach.commercialoffer.repository;

import by.bsuir.kursach.commercialoffer.model.OfferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferHistoryRepository extends JpaRepository<OfferHistory, Long> {
    List<OfferHistory> findByOfferId(Long offerId);
}