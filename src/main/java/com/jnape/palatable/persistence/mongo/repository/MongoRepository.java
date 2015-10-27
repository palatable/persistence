package com.jnape.palatable.persistence.mongo.repository;

import com.jnape.palatable.persistence.Entity;
import com.jnape.palatable.persistence.repository.Repository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface MongoRepository<Payload> extends Repository<Payload, ObjectId, DBObject> {

    @Override
    default Optional<Entity<Payload, ObjectId>> findOne() {
        return findOne(new BasicDBObject());
    }

    @Override
    default Iterable<Entity<Payload, ObjectId>> find() {
        return find(new BasicDBObject());
    }
}
