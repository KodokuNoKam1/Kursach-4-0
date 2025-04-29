package by.bsuir.kursach.commercialoffer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleMapsService {
    private final String apiKey = "ВАШ_API_КЛЮЧ";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getGeocode(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }
}