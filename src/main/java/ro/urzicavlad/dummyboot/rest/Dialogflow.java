package ro.urzicavlad.dummyboot.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2beta1WebhookResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dialog")
public class Dialogflow {

    private static final String API_KEY = "e4b9bfad3138808faa04f5e11f73fb73";

    @PostMapping("/webhook")
    public GoogleCloudDialogflowV2beta1WebhookResponse test(@RequestBody String response) throws IOException {
        GoogleCloudDialogflowV2beta1WebhookResponse webhookResponse =
                new ObjectMapper().readValue(response, GoogleCloudDialogflowV2beta1WebhookResponse.class);
        final LinkedHashMap<String, Object> queryResult = (LinkedHashMap<String, Object>) webhookResponse.get("queryResult");
        final LinkedHashMap<String, String> parameters = (LinkedHashMap<String, String>) queryResult.get("parameters");
        final String movie = parameters.get("movie");
        final var request = "https://api.themoviedb.org/3/search/movie?&api_key=" + API_KEY + "&page=1&query=" + movie;
        final var forEntity = new RestTemplate().getForEntity(request, Object.class);
        final var body = (Map<String, Object>) forEntity.getBody();
        final var results = (List<LinkedHashMap<String, Object>>) body.get("results");
        final var title = (String) results.get(0).get("title");
        final var overview = (String) results.get(0).get("overview");
        webhookResponse.setFulfillmentText(title + " have a interested story: " + overview);
        return webhookResponse;


    }

}
