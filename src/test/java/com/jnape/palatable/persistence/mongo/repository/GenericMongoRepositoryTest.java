package com.jnape.palatable.persistence.mongo.repository;

import com.jnape.palatable.persistence.mongo.serialization.BidirectionalJsonSerializer;
import com.mongodb.DBCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenericMongoRepositoryTest {

    @Mock private DBCollection                collection;
    @Mock private BidirectionalJsonSerializer serializer;

    @Test
    @SuppressWarnings("unchecked")
    public void initializesCorrectlyWhenGivenConcretePayloadClass() {
        createGenericSubtype(String.class);
    }

    @Test
    public void initializesCorrectlyWhenDerivingPayloadClassFromConcreteSubtype() {
        createConcreteSubtype();
    }

    @Test(expected = IllegalStateException.class)
    public void failsInitializationIfNoPayloadClassProvidedAndPayloadClassCannotBeDerived() {
        createGenericSubtype();
    }

    @SuppressWarnings("unchecked")
    private GenericMongoRepository<String> createConcreteSubtype() {
        return new GenericMongoRepository<String>(collection, serializer) {
        };
    }

    @SuppressWarnings("unchecked")
    private <Payload> GenericMongoRepository<Payload> createGenericSubtype() {
        return new GenericMongoRepository<Payload>(collection, serializer) {
        };
    }

    @SuppressWarnings("unchecked")
    private <Payload> GenericMongoRepository<Payload> createGenericSubtype(Class<Payload> payloadClass) {
        return new GenericMongoRepository<Payload>(payloadClass, collection, serializer) {
        };
    }
}
