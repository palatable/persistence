package com.jnape.palatable.persistence.mongo.repository;

import com.jnape.palatable.persistence.Entity;
import com.jnape.palatable.persistence.mongo.query.MongoQuery;
import com.jnape.palatable.persistence.mongo.serialization.BidirectionalJsonSerializer;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import static com.jnape.palatable.persistence.Entity.entity;
import static java.util.stream.StreamSupport.stream;

public abstract class GenericMongoRepository<Payload> implements MongoRepository<Payload, String> {

    private final DBCollection                collection;
    private final BidirectionalJsonSerializer serializer;

    public GenericMongoRepository(DBCollection collection, BidirectionalJsonSerializer serializer) {
        this.collection = collection;
        this.serializer = serializer;
    }

    @Override
    public Optional<Entity<Payload, String>> findOne(MongoQuery mongoQuery) {
        DBObject record = collection.findOne(mongoQuery.getCriteria());
        return Optional.ofNullable(record).map(this::toEntity);
    }

    @Override
    public Iterable<Entity<Payload, String>> find(MongoQuery mongoQuery) {
        DBCursor dbObjects = collection.find(mongoQuery.getCriteria());
        return () -> stream(dbObjects.spliterator(), false).map(this::toEntity).iterator();
    }

    private Entity<Payload, String> toEntity(DBObject dbObject) {
        Payload payload = serializer.deserialize(payloadClass(), dbObject.toString());
        return entity(payload, dbObject.get("_id").toString());
    }

    @Override
    public Entity<Payload, String> save(Payload payload) {
        DBObject dbObject = (DBObject) JSON.parse(serializer.serialize(payload));
        collection.save(dbObject);
        return entity(payload, dbObject.get("_id").toString());
    }

    @Override
    public Entity<Payload, String> save(Entity<Payload, String> entity) {
        DBObject dbObject = (DBObject) JSON.parse(serializer.serialize(entity.get()));
        dbObject.put("_id", new ObjectId(entity.id()));
        collection.save(dbObject);
        return entity;
    }

    @SuppressWarnings("unchecked")
    protected Class<Payload> payloadClass() {
        return (Class<Payload>) ((ParameterizedType) (getClass().getGenericSuperclass())).getActualTypeArguments()[0];
    }
}
