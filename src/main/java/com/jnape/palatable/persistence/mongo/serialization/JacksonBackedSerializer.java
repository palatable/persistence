package com.jnape.palatable.persistence.mongo.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static java.lang.String.format;

public class JacksonBackedSerializer<T> implements BidirectionalJsonSerializer<T> {

    private final ObjectMapper objectMapper;

    public JacksonBackedSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Could not serialize %s to json", t), e);
        }
    }

    @Override
    public T deserialize(Class<T> type, String value) {
        try {
            return objectMapper.readValue(value, type);
        } catch (IOException e) {
            throw new RuntimeException(format("Could not deserialize %s into %s", value, type.getSimpleName()), e);
        }
    }
}
