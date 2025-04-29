package by.bsuir.kursach.commercialoffer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "offer_items")
@Data
public class OfferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
}