package testsupport.fixture;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import testsupport.domain.Gender;
import testsupport.domain.Person;

import java.io.IOException;


public class ObjectMappers {

    public static ObjectMapper configuredForTestFixtures() {
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
                                              DeserializationContext ctxt) throws IOException, JsonProcessingException {
                        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
                        String firstName = jsonNode.get("firstName").asText();
                        String gender = jsonNode.get("gender").asText();
                        return Person.person(firstName, Gender.valueOf(gender));
                    }
                });

        return new ObjectMapper()
                .registerModule(testFixtures)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
