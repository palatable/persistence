package com.jnape.palatable.persistence.mongo.repository;

import com.jnape.palatable.persistence.Entity;
import com.jnape.palatable.persistence.mongo.query.MongoQuery;
import com.jnape.palatable.persistence.repository.Repository;

import java.util.Optional;

public interface MongoRepository<Payload, Id> extends Repository<Payload, Id, MongoQuery> {

    @Override
    default Optional<Entity<Payload, Id>> findOne() {
        return findOne(MongoQuery.query());
    }

    @Override
    default Iterable<Entity<Payload, Id>> find() {
        return find(MongoQuery.query());
    }
}
