package by.bsuir.kursach.commercialoffer.controller;

import by.bsuir.kursach.commercialoffer.repository.OfferRepository;
import by.bsuir.kursach.commercialoffer.repository.OfferHistoryRepository;
import by.bsuir.kursach.commercialoffer.service.PdfService;
import by.bsuir.kursach.commercialoffer.service.OfferHistoryService;
import by.bsuir.kursach.commercialoffer.service.CsvImportService;
import by.bsuir.kursach.commercialoffer.model.Offer;
import by.bsuir.kursach.commercialoffer.model.OfferHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "Offers", description = "API for managing commercial offers")
public class OfferController {

    private final OfferRepository offerRepository;
    private final PdfService pdfService;
    private final OfferHistoryService historyService;
    private final CsvImportService csvImportService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public OfferController(OfferRepository offerRepository, PdfService pdfService, OfferHistoryService historyService, CsvImportService csvImportService, NotificationService notificationService, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.pdfService = pdfService;
        this.historyService = historyService;
        this.csvImportService = csvImportService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/search")
    @Operation(summary = "Search offers", description = "Searches offers by title, currency, or amount range")
    public ResponseEntity<List<Offer>> searchOffers(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long currencyId,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) {
        if (title != null) {
            return ResponseEntity.ok(offerRepository.findByTitleContaining(title));
        }
        if (currencyId != null) {
            return ResponseEntity.ok(offerRepository.findByCurrencyId(currencyId));
        }
        if (minAmount != null && maxAmount != null) {
            return ResponseEntity.ok(offerRepository.findByAmountBetween(minAmount, maxAmount));
        }
        return ResponseEntity.ok(offerRepository.findAll());
    }

    @PostMapping("/import")
    @Operation(summary = "Import offers from CSV", description = "Imports multiple offers from a CSV file")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Offer>> importOffers(@RequestParam("file") MultipartFile file) throws IOException, CsvValidationException {
        return ResponseEntity.ok(csvImportService.importOffersFromCsv(file));
    }

    @PostMapping
    @Operation(summary = "Create an offer", description = "Creates a new commercial offer")
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer) {
        Offer savedOffer = offerRepository.save(offer);
        historyService.logAction(savedOffer, "CREATED", "Offer created with title: " + offer.getTitle());
        return ResponseEntity.ok(savedOffer);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an offer by ID", description = "Returns a specific commercial offer")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id) {
        return offerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an offer", description = "Updates an existing commercial offer")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer updatedOffer) {
        return offerRepository.findById(id)
                .map(existingOffer -> {
                    existingOffer.setTitle(updatedOffer.getTitle());
                    existingOffer.setDescription(updatedOffer.getDescription());
                    existingOffer.setAmount(updatedOffer.getAmount());
                    existingOffer.setCurrency(updatedOffer.getCurrency());
                    Offer savedOffer = offerRepository.save(existingOffer);
                    historyService.logAction(savedOffer, "UPDATED", "Offer updated with title: " + updatedOffer.getTitle());
                    return ResponseEntity.ok(savedOffer);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an offer", description = "Deletes a specific commercial offer")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        return offerRepository.findById(id)
                .map(offer -> {
                    offerRepository.deleteById(id);
                    historyService.logAction(offer, "DELETED", "Offer deleted with title: " + offer.getTitle());
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/bulk")
    @Operation(summary = "Delete multiple offers", description = "Deletes multiple offers by IDs (ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteMultipleOffers(@RequestBody List<Long> ids) {
        offerRepository.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Get offer history", description = "Returns the history of changes for a specific offer")
    public ResponseEntity<List<OfferHistory>> getOfferHistory(@PathVariable Long id) {
        return ResponseEntity.ok(historyRepository.findByOfferId(id));
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get offer statistics", description = "Returns statistics about offers by currency")
    public ResponseEntity<List<OfferStatistics>> getOfferStatistics() {
        List<Object[]> stats = offerRepository.getOfferStatistics();
        List<OfferStatistics> result = stats.stream()
                .map(row -> {
                    OfferStatistics stat = new OfferStatistics();
                    stat.setCurrencyCode((String) row[0]);
                    stat.setCount((Long) row[1]);
                    stat.setTotalAmount((Double) row[2]);
                    return stat;
                })
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/pdf")
    @Operation(summary = "Export offer as PDF", description = "Generates a PDF for a specific commercial offer")
    public ResponseEntity<byte[]> exportOfferAsPdf(@PathVariable Long id) {
        return offerRepository.findById(id)
                .map(offer -> {
                    byte[] pdf = pdfService.generateOfferPdf(offer);
                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=offer_" + id + ".pdf")
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(pdf);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create an offer", description = "Creates a new commercial offer")
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer) {
        Offer savedOffer = offerRepository.save(offer);
        historyService.logAction(savedOffer, "CREATED", "Offer created with title: " + offer.getTitle());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                .getId();
        notificationService.createNotification(userId, "New offer created: " + offer.getTitle());
        return ResponseEntity.ok(savedOffer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an offer", description = "Updates an existing commercial offer")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer updatedOffer) {
        return offerRepository.findById(id)
                .map(existingOffer -> {
                    existingOffer.setTitle(updatedOffer.getTitle());
                    existingOffer.setDescription(updatedOffer.getDescription());
                    existingOffer.setAmount(updatedOffer.getAmount());
                    existingOffer.setCurrency(updatedOffer.getCurrency());
                    Offer savedOffer = offerRepository.save(existingOffer);
                    historyService.logAction(savedOffer, "UPDATED", "Offer updated with title: " + updatedOffer.getTitle());
                    String username = SecurityContextHolder.getContext().getAuthentication().getName();
                    Long userId = userRepository.findByUsername(username)
                            .orElseThrow(() -> new IllegalArgumentException("User not found"))
                            .getId();
                    notificationService.createNotification(userId, "Offer updated: " + updatedOffer.getTitle());
                    return ResponseEntity.ok(savedOffer);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
