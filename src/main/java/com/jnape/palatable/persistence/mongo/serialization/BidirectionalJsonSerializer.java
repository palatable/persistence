package com.jnape.palatable.persistence.mongo.serialization;

public interface BidirectionalJsonSerializer<Value> {
    String serialize(Value value);

    Value deserialize(Class<Value> type, String value);
}
