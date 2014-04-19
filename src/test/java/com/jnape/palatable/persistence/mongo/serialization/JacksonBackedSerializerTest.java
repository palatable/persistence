package com.jnape.palatable.persistence.mongo.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import testsupport.fixture.Item;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class JacksonBackedSerializerTest {

    private BidirectionalJsonSerializer<Item> serializer;

    @Before
    public void setUp() {
        serializer = new JacksonBackedSerializer<>(new ObjectMapper());
    }

    @Test
    public void serializesAnObjectToJson() throws JSONException {
        JSONObject itemJson = new JSONObject() {{
            put("label", Item.A.getLabel());
        }};

        JSONAssert.assertEquals(itemJson.toString(), serializer.serialize(Item.A), true);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RuntimeException.class)
    public void throwsExceptionIfSerializationFails() throws JsonProcessingException {
        Item problematicItem = mock(Item.class);
        serializer.serialize(problematicItem);
    }

    @Test
    public void deserializesJsonIntoCorrespondingType() throws JSONException {
        JSONObject itemJson = new JSONObject() {{
            put("label", Item.A.getLabel());
        }};

        assertThat(serializer.deserialize(Item.class, itemJson.toString()), is(Item.A));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RuntimeException.class)
    public void throwsExceptionIfDeserializationFails() {
        serializer.deserialize(Item.class, "invalid");
    }
}
