package com.jnape.palatable.persistence.repository;

import com.jnape.palatable.persistence.Entity;

import java.util.Optional;

public interface Repository<Payload, Id, Query> {

    Optional<Entity<Payload, Id>> findOne();

    Optional<Entity<Payload, Id>> findOne(Query query);

    Iterable<Entity<Payload, Id>> find();

    Iterable<Entity<Payload, Id>> find(Query query);

    Entity<Payload, Id> save(Payload payload);

    Entity<Payload, Id> save(Entity<Payload, Id> entity);
}
