package by.bsuir.kursach.commercialoffer.repository;

import by.bsuir.kursach.commercialoffer.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OfferRepository extends PagingAndSortingRepository<Offer, Long> {
    @Query("SELECT o FROM Offer o WHERE LOWER(o.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Offer> findByTitleContaining(@Param("title") String title);

    List<Offer> findByCurrencyId(Long currencyId);

    List<Offer> findByAmountBetween(Double minAmount, Double maxAmount);

    @Query("SELECT o.currency.code, COUNT(o), SUM(o.amount) FROM Offer o GROUP BY o.currency.code")
    List<Object[]> getOfferStatistics();

    List<Offer> findByCategoryId(Long categoryId);
}