package sptech.school.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class GeocodingResponse {
    private String responseBody;

    public GeocodingResponse(String responseBody) {
        this.responseBody = responseBody;
    }

}
