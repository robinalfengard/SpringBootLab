package com.example.springbootlabmessages.GetRandomInfoApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class GetRandomInfoService {

    private final RestClient restClient;

    public GetRandomInfoService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getRandomInfo() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = restClient
                .get()
                .uri("https://uselessfacts.jsph.pl/api/v2/facts/random")
                .retrieve()
                .body(String.class);



        JsonNode jsonArray = objectMapper.readTree(response);
        return jsonArray.get("text").asText();


    }
}
