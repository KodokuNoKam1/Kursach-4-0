package by.bsuir.kursach.commercialoffer.controller;

import by.bsuir.kursach.commercialoffer.dto.RegisterRequest;
import by.bsuir.kursach.commercialoffer.model.Offer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuditLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    public void setup() throws Exception {
        RegisterRequest adminRequest = new RegisterRequest();
        adminRequest.setUsername("admin");
        adminRequest.setPassword("password");
        adminRequest.setRole("ADMIN");
        String response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRequest)))
                .andReturn().getResponse().getContentAsString();
        adminToken = objectMapper.readValue(response, Map.class).get("token").toString();
    }

    @Test
    public void testGetAllAuditLogs() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/audit?page=0&size=10")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].action").value("CREATE_OFFER"));
    }
}