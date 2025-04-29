package by.bsuir.kursach.commercialoffer.controller;

import by.bsuir.kursach.commercialoffer.model.Currency;
import by.bsuir.kursach.commercialoffer.repository.CurrencyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@Tag(name = "Currencies", description = "API for managing currencies")
public class CurrencyController {

    private final CurrencyRepository currencyRepository;

    public CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    @Operation(summary = "Get all currencies", description = "Returns a list of all currencies")
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return ResponseEntity.ok(currencyRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a currency", description = "Creates a new currency")
    public ResponseEntity<Currency> createCurrency(@RequestBody Currency currency) {
        return ResponseEntity.ok(currencyRepository.save(currency));
    }
}