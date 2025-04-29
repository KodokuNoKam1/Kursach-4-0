package by.bsuir.kursach.commercialoffer.controller;

import by.bsuir.kursach.commercialoffer.repository.OfferRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "Offers", description = "API for managing commercial offers")
public class OfferController {

    private final OfferRepository offerRepository;

    public OfferController(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
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
}