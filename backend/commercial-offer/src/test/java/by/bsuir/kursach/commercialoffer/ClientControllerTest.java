package by.bsuir.kursach.commercialoffer;

import by.bsuir.kursach.commercialoffer.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateClient() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");

        ResponseEntity<Client> response = restTemplate.postForEntity("/api/clients", client, Client.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}