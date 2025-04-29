package by.bsuir.kursach.commercialoffer.dto;

import lombok.Data;

@Data
public class OfferStatistics {
    private String currencyCode;
    private Long count;
    private Double totalAmount;
}