package com.jnape.palatable.persistence.mongo.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fakemongo.junit.FongoRule;
import com.jnape.palatable.persistence.mongo.repository.GenericMongoRepository;
import com.jnape.palatable.persistence.mongo.repository.MongoRepository;
import com.jnape.palatable.persistence.mongo.serialization.BidirectionalJsonSerializer;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import testsupport.domain.Person;
import testsupport.fixture.ObjectMappers;

import static com.jnape.palatable.persistence.mongo.query.MongoQuery.query;
import static org.junit.Assert.assertThat;
import static testsupport.converters.DBObjectConverter.convertToDBObject;
import static testsupport.fixture.People.*;
import static testsupport.matchers.IsEntityCollectionContainingPayload.hasPayloads;

public class MongoQueryIntegrationTest {

    @Rule public FongoRule fongoRule = new FongoRule();

    private DBCollection collection;

    private MongoRepository<Person, String> repository;

    @Before
    public void setUp() {
        collection = fongoRule.newCollection();
        ObjectMapper objectMapper = ObjectMappers.configuredForTestFixtures();
        repository = new GenericMongoRepository<Person>(collection, new BidirectionalJsonSerializer(objectMapper)) {
        };
    }

    @Test
    public void eqMatchesOnFieldEquality() {
        collection.save(convertToDBObject(ALICE));
        collection.save(convertToDBObject(BOB));

        assertThat(repository.find(query().eq("firstName", ALICE.getFirstName())), hasPayloads(ALICE));
        assertThat(repository.find(query().eq("firstName", BOB.getFirstName())), hasPayloads(BOB));
    }

    @Test
    public void $existsMatchesOnFieldExistence() {
        DBObject chelsea = convertToDBObject(CHELSEA);
        chelsea.put("uniqueField", 1);
        collection.save(chelsea);

        collection.save(convertToDBObject(DAVID));
        collection.save(convertToDBObject(ELEANOR));

        assertThat(repository.find(query().$exists("uniqueField", true)), hasPayloads(CHELSEA));
    }
}
