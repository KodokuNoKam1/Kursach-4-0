package by.bsuir.kursach.commercialoffer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "currencies")
@Data
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Например, USD, BYN

    @Column(nullable = false)
    private String name; // Например, US Dollar, Belarusian Ruble
}