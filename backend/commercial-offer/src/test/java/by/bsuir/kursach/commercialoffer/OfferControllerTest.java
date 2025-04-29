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

    @Test
    public void testSearchOffersByTitle() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/offers/search?title=Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Offer"));
    }

    @Test
    public void testSearchOffersByCurrency() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/offers/search?currencyId={id}", currency.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Offer"));
    }

    @Test
    public void testSearchOffersByAmountRange() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/offers/search?minAmount=500&maxAmount=1500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Offer"));
    }

    @Test
    public void testDeleteMultipleOffers() throws Exception {
        Offer offer1 = new Offer();
        offer1.setTitle("Test Offer 1");
        offer1.setDescription("Description");
        offer1.setAmount(1000.0);
        offer1.setCurrency(currency);
        String response1 = mockMvc.perform(post("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer1)))
                .andReturn().getResponse().getContentAsString();
        Long id1 = objectMapper.readValue(response1, Offer.class).getId();

        Offer offer2 = new Offer();
        offer2.setTitle("Test Offer 2");
        offer2.setDescription("Description");
        offer2.setAmount(2000.0);
        offer2.setCurrency(currency);
        String response2 = mockMvc.perform(post("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer2)))
                .andReturn().getResponse().getContentAsString();
        Long id2 = objectMapper.readValue(response2, Offer.class).getId();

        mockMvc.perform(delete("/api/offers/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(id1, id2))))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetOfferHistory() throws Exception {
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

        mockMvc.perform(get("/api/offers/{id}/history", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].action").value("CREATED"));
    }

    @Test
    public void testImportOffersFromCsv() throws Exception {
        String csvContent = "title,description,amount,currencyId\n" +
                "\"Imported Offer\",\"Imported Description\",1500.0," + currency.getId();
        MockMultipartFile file = new MockMultipartFile("file", "offers.csv", "text/csv", csvContent.getBytes());

        mockMvc.perform(multipart("/api/offers/import").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Imported Offer"));
    }

    @Test
    public void testGetOfferStatistics() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/offers/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$[0].count").value(1))
                .andExpect(jsonPath("$[0].totalAmount").value(1000.0));
    }

    @Test
    public void testExportOffersToExcel() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/offers/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=offers.xlsx"));
    }

    @Test
    public void testGetOffersPaginated() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/offers?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Offer"));
    }
}