package com.jnape.palatable.persistence.mongo.query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoQuery {

    DBObject criteria;

    public MongoQuery eq(String key, String value) {
        criteria.put(key, value);
        return this;
    }

    public MongoQuery $exists(String key, boolean exists) {
        criteria.put(key, new BasicDBObject("$exists", exists));
        return this;
    }

    public static MongoQuery query() {
        return new MongoQuery(new BasicDBObject());
    }
}
