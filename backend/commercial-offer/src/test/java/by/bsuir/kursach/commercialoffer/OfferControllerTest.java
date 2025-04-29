package by.bsuir.kursach.commercialoffer;

import by.bsuir.kursach.commercialoffer.model.Currency;
import by.bsuir.kursach.commercialoffer.model.Offer;
import by.bsuir.kursach.commercialoffer.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Currency currency;

    @BeforeEach
    public void setup() {
        currency = new Currency();
        currency.setCode("USD");
        currency.setName("US Dollar");
        currency = currencyRepository.save(currency);
    }

    @Test
    public void testCreateOffer() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);

        mockMvc.perform(post("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Offer"));
    }

    @Test
    public void testGetAllOffers() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Offer"));
    }

    @Test
    public void testGetOfferById() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        String response = mockMvc.perform(post("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(response, Offer.class).getId();

        mockMvc.perform(get("/api/offers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Offer"));
    }

    @Test
    public void testUpdateOffer() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        String response = mockMvc.perform(post("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(response, Offer.class).getId();

        offer.setTitle("Updated Offer");
        mockMvc.perform(put("/api/offers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Offer"));
    }

    @Test
    public void testDeleteOffer() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        String response = mockMvc.perform(post("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(response, Offer.class).getId();

        mockMvc.perform(delete("/api/offers/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/offers/{id}", id))
                .andExpect(status().isNotFound());
    }
}