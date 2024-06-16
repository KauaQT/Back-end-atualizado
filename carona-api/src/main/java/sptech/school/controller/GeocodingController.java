package sptech.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.AddressDTO;
import sptech.school.service.GeocodingService;

@RestController
@RequestMapping("/geocoding")
public class GeocodingController {

    @Autowired
    private GeocodingService geocodingService;


}
