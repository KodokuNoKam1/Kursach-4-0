package by.bsuir.kursach.commercialoffer.controller;

import by.bsuir.kursach.commercialoffer.repository.OfferRepository;
import by.bsuir.kursach.commercialoffer.service.PdfService;
import by.bsuir.kursach.commercialoffer.model.Offer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "Offers", description = "API for managing commercial offers")
public class OfferController {

    private final OfferRepository offerRepository;
    private final PdfService pdfService;

    public OfferController(OfferRepository offerRepository, PdfService pdfService) {
        this.offerRepository = offerRepository;
        this.pdfService = pdfService;
    }

    @GetMapping
    @Operation(summary = "Get all offers", description = "Returns a list of all commercial offers")
    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Create an offer", description = "Creates a new commercial offer")
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer) {
        return ResponseEntity.ok(offerRepository.save(offer));
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
                    return ResponseEntity.ok(offerRepository.save(existingOffer));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an offer", description = "Deletes a specific commercial offer")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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
}
