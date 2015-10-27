package com.jnape.palatable.persistence.mongo.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import testsupport.fixture.Item;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class GsonBackedSerializerTest {

    private GsonBackedSerializer<Item> serializer;

    @Before
    public void setUp() {
        serializer = new GsonBackedSerializer<>(new Gson());
    }

    @Test
    public void serializesAnObjectToJson() throws JSONException {
        JSONObject itemJson = new JSONObject() {{
            put("label", Item.A.getLabel());
        }};

        JSONAssert.assertEquals(itemJson.toString(), serializer.serialize(Item.A), true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throwsExceptionIfSerializationFails() {
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

    @Test(expected = JsonSyntaxException.class)
    public void throwsExceptionIfDeserializationFails() {
        serializer.deserialize(Item.class, "invalid");
    }
}
