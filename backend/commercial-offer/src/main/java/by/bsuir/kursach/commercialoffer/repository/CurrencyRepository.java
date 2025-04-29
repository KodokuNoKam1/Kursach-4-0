package by.bsuir.kursach.commercialoffer.repository;

import by.bsuir.kursach.commercialoffer.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}