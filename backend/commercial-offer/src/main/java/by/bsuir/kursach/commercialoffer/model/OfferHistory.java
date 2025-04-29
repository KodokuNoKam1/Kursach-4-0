package by.bsuir.kursach.commercialoffer.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "offer_history")
@Data
public class OfferHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @Column(nullable = false)
    private String action; // Например, "CREATED", "UPDATED", "DELETED"

    @Column
    private String details;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
