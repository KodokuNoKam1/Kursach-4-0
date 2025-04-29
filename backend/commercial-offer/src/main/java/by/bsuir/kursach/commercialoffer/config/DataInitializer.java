package by.bsuir.kursach.commercialoffer.config;

import by.bsuir.kursach.commercialoffer.model.Currency;
import by.bsuir.kursach.commercialoffer.repository.CurrencyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CurrencyRepository currencyRepository;

    public DataInitializer(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void run(String... args) {
        if (currencyRepository.count() == 0) {
            Currency usd = new Currency();
            usd.setCode("USD");
            usd.setName("US Dollar");
            currencyRepository.save(usd);

            Currency byn = new Currency();
            byn.setCode("BYN");
            byn.setName("Belarusian Ruble");
            currencyRepository.save(byn);
        }
    }
}