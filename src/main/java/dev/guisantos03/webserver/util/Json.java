package dev.guisantos03.webserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    private static final ObjectMapper objectMapper = defaultObjectMapper();

    private Json() {}

    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // disable the failure when finds an unknown property
        return om;
    }

    public static JsonNode parse(String json) throws JsonProcessingException {
        return objectMapper.readTree(json);
    }

    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        return objectMapper.treeToValue(node, clazz);
    }
}
