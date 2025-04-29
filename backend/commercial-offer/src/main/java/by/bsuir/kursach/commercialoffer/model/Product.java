package by.bsuir.kursach.commercialoffer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}