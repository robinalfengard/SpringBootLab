package com.example.springbootlabmessages.Translation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TranslationService {

    private final RestClient restClient;



    public TranslationService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getLanguage(String text) throws JsonProcessingException {
        String jsonBody  = "{\"q\":\"" + text + "\"}";
        ObjectMapper objectMapper = new ObjectMapper();

        String response = restClient.post()
        .uri("http://localhost:5000/detect")
        .contentType(MediaType.APPLICATION_JSON)
        .body(jsonBody)
        .retrieve()
        .body(String.class);

        JsonNode jsonArray = objectMapper.readTree(response);

        return jsonArray.get(0).get("language").asText();
    }

}
