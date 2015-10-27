package com.jnape.palatable.persistence.mongo.serialization;

import com.google.gson.Gson;

public final class GsonBackedSerializer<T> implements BidirectionalJsonSerializer<T> {

    private final Gson gson;

    public GsonBackedSerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String serialize(T t) {
        return gson.toJson(t);
    }

    @Override
    public T deserialize(Class<T> type, String value) {
        return gson.fromJson(value, type);
    }
}
