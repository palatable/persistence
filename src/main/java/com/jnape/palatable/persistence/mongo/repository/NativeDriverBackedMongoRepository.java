package com.jnape.palatable.persistence.mongo.repository;

import com.jnape.palatable.persistence.Entity;
import com.jnape.palatable.persistence.mongo.serialization.BidirectionalJsonSerializer;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;

import java.util.Optional;

import static com.jnape.palatable.persistence.Entity.entity;
import static java.util.stream.StreamSupport.stream;

public class NativeDriverBackedMongoRepository<Payload> implements MongoRepository<Payload> {

    private final Class<Payload>                       payloadClass;
    private final DBCollection                         collection;
    private final BidirectionalJsonSerializer<Payload> payloadSerializer;

    public NativeDriverBackedMongoRepository(Class<Payload> payloadClass,
                                             DBCollection collection,
                                             BidirectionalJsonSerializer<Payload> payloadSerializer) {
        this.payloadClass = payloadClass;
        this.collection = collection;
        this.payloadSerializer = payloadSerializer;
    }

    @Override
    public Optional<Entity<Payload, ObjectId>> findOne(DBObject query) {
        DBObject record = collection.findOne(query);
        return Optional.ofNullable(record).map(this::toEntity);
    }

    @Override
    public Iterable<Entity<Payload, ObjectId>> find(DBObject query) {
        DBCursor dbObjects = collection.find(query);
        return () -> stream(dbObjects.spliterator(), false).map(this::toEntity).iterator();
    }

    @Override
    public Entity<Payload, ObjectId> save(Payload payload) {
        DBObject dbObject = (DBObject) JSON.parse(payloadSerializer.serialize(payload));
        dbObject.put("_id", ObjectId.get());
        collection.save(dbObject);
        return toEntity(dbObject);
    }

    @Override
    public Entity<Payload, ObjectId> save(Entity<Payload, ObjectId> entity) {
        DBObject dbObject = (DBObject) JSON.parse(payloadSerializer.serialize(entity.get()));
        dbObject.put("_id", entity.id());
        collection.save(dbObject);
        return entity;
    }

    protected final Entity<Payload, ObjectId> toEntity(DBObject dbObject) {
        Payload payload = payloadSerializer.deserialize(payloadClass, dbObject.toString());
        return entity(payload, (ObjectId) dbObject.get("_id"));
    }
}
