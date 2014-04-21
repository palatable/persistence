package com.jnape.palatable.persistence.mongo.repository;

import com.jnape.palatable.persistence.Entity;
import com.jnape.palatable.persistence.mongo.serialization.BidirectionalJsonSerializer;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import static com.jnape.palatable.persistence.Entity.entity;
import static java.util.stream.StreamSupport.stream;

public abstract class GenericMongoRepository<Payload> implements MongoRepository<Payload, String> {

    private final Class<Payload>                       payloadClass;
    private final DBCollection                         collection;
    private final BidirectionalJsonSerializer<Payload> serializer;

    public GenericMongoRepository(Class<Payload> payloadClass,
                                  DBCollection collection,
                                  BidirectionalJsonSerializer<Payload> serializer) {
        this.payloadClass = payloadClass;
        this.collection = collection;
        this.serializer = serializer;
    }

    public GenericMongoRepository(DBCollection collection, BidirectionalJsonSerializer<Payload> serializer) {
        this.collection = collection;
        this.serializer = serializer;
        payloadClass = derivePayloadClass();
    }

    @Override
    public final Optional<Entity<Payload, String>> findOne(DBObject query) {
        DBObject record = collection.findOne(query);
        return Optional.ofNullable(record).map(this::toEntity);
    }

    @Override
    public final Iterable<Entity<Payload, String>> find(DBObject query) {
        DBCursor dbObjects = collection.find(query);
        return () -> stream(dbObjects.spliterator(), false).map(this::toEntity).iterator();
    }

    @Override
    public final Entity<Payload, String> save(Payload payload) {
        DBObject dbObject = (DBObject) JSON.parse(serializer.serialize(payload));
        collection.save(dbObject);
        return entity(payload, dbObject.get("_id").toString());
    }

    @Override
    public final Entity<Payload, String> save(Entity<Payload, String> entity) {
        DBObject dbObject = (DBObject) JSON.parse(serializer.serialize(entity.get()));
        dbObject.put("_id", new ObjectId(entity.id()));
        collection.save(dbObject);
        return entity;
    }

    protected final Entity<Payload, String> toEntity(DBObject dbObject) {
        Payload payload = serializer.deserialize(payloadClass, dbObject.toString());
        return entity(payload, dbObject.get("_id").toString());
    }

    @SuppressWarnings("unchecked")
    private Class<Payload> derivePayloadClass() {
        Type payloadType = ((ParameterizedType) (getClass().getGenericSuperclass())).getActualTypeArguments()[0];
        if (payloadType instanceof Class)
            return (Class<Payload>) payloadType;

        throw new IllegalStateException(
                "Could not derive concrete class from Payload type due to limitations with Java Generics; consider either:" +
                        "\n" +
                        "\t1) Calling the constructor with the concrete Payload class specified, or" +
                        "\n" +
                        "\t2) Substituting a concrete type for the Payload type argument in your Repository subclass"
        );
    }
}
