package com.jnape.palatable.persistence.mongo.serialization.fixture;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jnape.palatable.persistence.fixture.Gender;
import com.jnape.palatable.persistence.fixture.Person;
import com.jnape.palatable.persistence.mongo.serialization.JacksonBackedSerializer;

import java.io.IOException;

public class Serializers {

    public static <T> JacksonBackedSerializer<T> testJacksonSerializer() {
        Module testFixtures = new SimpleModule("Test Fixtures", Version.unknownVersion())
                .addSerializer(Person.class, new JsonSerializer<Person>() {
                    @Override
                    public void serialize(Person person, JsonGenerator jsonGenerator,
                                          SerializerProvider provider) throws IOException {
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeStringField("firstName", person.getFirstName());
                        jsonGenerator.writeStringField("gender", person.getGender().name());
                        jsonGenerator.writeEndObject();
                    }
                })
                .addDeserializer(Person.class, new JsonDeserializer<Person>() {
                    @Override
                    public Person deserialize(JsonParser jsonParser,
                                              DeserializationContext ctxt) throws IOException {
                        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
                        String firstName = jsonNode.get("firstName").asText();
                        String gender = jsonNode.get("gender").asText();
                        return Person.person(firstName, Gender.valueOf(gender));
                    }
                });

        ObjectMapper configuredObjectMapper = new ObjectMapper()
                .registerModule(testFixtures)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return new JacksonBackedSerializer<>(configuredObjectMapper);
    }
}
