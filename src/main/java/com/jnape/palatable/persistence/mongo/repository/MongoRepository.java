package com.jnape.palatable.persistence.mongo.repository;

import com.jnape.palatable.persistence.Entity;
import com.jnape.palatable.persistence.repository.Repository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Optional;

public interface MongoRepository<Payload, Id> extends Repository<Payload, Id, DBObject> {

    @Override
    default Optional<Entity<Payload, Id>> findOne() {
        return findOne(new BasicDBObject());
    }

    @Override
    default Iterable<Entity<Payload, Id>> find() {
        return find(new BasicDBObject());
    }
}
