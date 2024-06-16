package sptech.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sptech.school.Response.GeocodingResponse;
import sptech.school.dto.AddressDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeocodingService {

    @Value("${google.maps.api.key}")
    private static String apiKey;

    private final RestTemplate restTemplate;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }




}
