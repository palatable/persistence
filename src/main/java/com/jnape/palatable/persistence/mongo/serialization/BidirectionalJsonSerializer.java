package com.jnape.palatable.persistence.mongo.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static java.lang.String.format;

public class BidirectionalJsonSerializer {

    private final ObjectMapper objectMapper;

    public BidirectionalJsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Could not serialize %s to json", object), e);
        }
    }

    public <T> T deserialize(Class<T> type, String value) {
        try {
            return objectMapper.readValue(value, type);
        } catch (IOException e) {
            throw new RuntimeException(format("Could not deserialize %s into %s", value, type.getSimpleName()), e);
        }
    }
}
