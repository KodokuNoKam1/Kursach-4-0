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
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    @BeforeEach
    public void setup() throws Exception {
        RegisterRequest userRequest = new RegisterRequest();
        userRequest.setUsername("user");
        userRequest.setPassword("password");
        userRequest.setRole("USER");
        String response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andReturn().getResponse().getContentAsString();
        userToken = objectMapper.readValue(response, Map.class).get("token").toString();
    }

    @Test
    public void testGetUnreadNotifications() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(get("/api/notifications")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("New offer created: Test Offer"));
    }

    @Test
    public void testMarkNotificationAsRead() throws Exception {
        Offer offer = new Offer();
        offer.setTitle("Test Offer");
        offer.setDescription("Description");
        offer.setAmount(1000.0);
        offer.setCurrency(currency);
        mockMvc.perform(post("/api/offers")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)));

        mockMvc.perform(put("/api/notifications/1/read")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/notifications")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}