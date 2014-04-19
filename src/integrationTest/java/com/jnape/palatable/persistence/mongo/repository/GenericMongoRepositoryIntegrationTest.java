package com.jnape.palatable.persistence.mongo.repository;

import com.github.fakemongo.junit.FongoRule;
import com.jnape.palatable.persistence.Entity;
import com.jnape.palatable.persistence.fixture.Person;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.persistence.Entity.entity;
import static com.jnape.palatable.persistence.fixture.People.*;
import static com.jnape.palatable.persistence.mongo.serialization.fixture.Serializers.testJacksonSerializer;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static testsupport.matchers.IsEntityCollectionContainingPayload.hasPayload;
import static testsupport.matchers.IsEntityCollectionContainingPayload.hasPayloads;

public class GenericMongoRepositoryIntegrationTest {

    @Rule public FongoRule fongoRule = new FongoRule();

    private DBCollection collection;

    private GenericMongoRepository<Person> repository;

    @Before
    public void setUp() {
        collection = fongoRule.newCollection();
        repository = new GenericMongoRepository<Person>(collection, testJacksonSerializer()) {
        };
    }

    @Test
    public void findOneWithoutQueryFetchesFirstRecord() {
        ObjectId id = new ObjectId();

        collection.save(new BasicDBObject() {{
            put("_id", id);
            put("firstName", "Bob");
            put("gender", "MALE");
        }});

        assertThat(repository.findOne(), is(Optional.of(entity(BOB, id.toString()))));
    }

    @Test
    public void findOneWithQueryFetchesFirstRecordMatchingQuery() {
        ObjectId chelseaId = new ObjectId();
        collection.save(new BasicDBObject() {{
            put("_id", chelseaId);
            put("firstName", "Chelsea");
            put("gender", "FEMALE");
        }});

        ObjectId davidId = new ObjectId();
        collection.save(new BasicDBObject() {{
            put("_id", davidId);
            put("firstName", "David");
            put("gender", "MALE");
        }});

        assertThat(
                repository.findOne(QueryBuilder.start("firstName").is("David").get()),
                is(Optional.of(entity(DAVID, davidId.toString())))
        );

        assertThat(
                repository.findOne(QueryBuilder.start("firstName").is("Chelsea").get()),
                is(Optional.of(entity(CHELSEA, chelseaId.toString())))
        );
    }

    @Test
    public void findOneIsNothingIfNoMatchingRecords() {
        assertThat(repository.findOne(), is(Optional.empty()));
    }

    @Test
    public void findWithoutCriteriaFetchesAllRecords() {
        collection.insert(asList(
                new BasicDBObjectBuilder()
                        .add("firstName", "Bob")
                        .add("gender", "MALE")
                        .get(),
                new BasicDBObjectBuilder()
                        .add("firstName", "Chelsea")
                        .add("gender", "FEMALE")
                        .get()
        ));

        assertThat(repository.find(), hasPayloads(BOB, CHELSEA));
    }

    @Test
    public void findWithQueryFetchesAllRecordsMatchingQuery() {
        collection.insert(asList(
                new BasicDBObjectBuilder()
                        .add("firstName", "Eleanor")
                        .add("gender", "FEMALE")
                        .get(),
                new BasicDBObjectBuilder()
                        .add("firstName", "David")
                        .add("gender", "MALE")
                        .get(),
                new BasicDBObjectBuilder()
                        .add("firstName", "Alice")
                        .add("gender", "FEMALE")
                        .get()
        ));

        assertThat(repository.find(QueryBuilder.start("gender").is("FEMALE").get()), hasPayloads(ELEANOR, ALICE));
        assertThat(repository.find(QueryBuilder.start("gender").is("MALE").get()), hasPayload(DAVID));
    }

    @Test
    public void findIsEmptyIfNoMatchingRecords() {
        assertThat(repository.find().iterator().hasNext(), is(false));
    }

    @Test
    public void saveInsertsRecordAndReturnsEntityRepresentation() {
        Person alice = ALICE;
        Entity<Person, String> entity = repository.save(alice);

        DBObject savedRecord = collection.findOne();
        assertNotNull(savedRecord);

        assertThat(entity.get(), is(alice));
        assertEquals(entity.id(), savedRecord.get("_id").toString());
    }

    @Test
    public void saveUpdatesRecordWithEntityId() {
        Entity<Person, String> ethan = repository.save(ETHAN);
        DBObject record = collection.findOne();
        assertThat(record.get("firstName"), is("Ethan"));
        assertThat(record.get("gender"), is("MALE"));

        repository.save(entity(ELEANOR, ethan.id()));
        DBObject newRecord = collection.findOne();
        assertThat(newRecord.get("firstName"), is("Eleanor"));
        assertThat(newRecord.get("gender"), is("FEMALE"));

        assertThat(collection.count(), is(1L));
    }

    @Test
    public void saveInsertsNewRecordWithEntityIdIfNoPreviousRecordExists() {
        String bobId = ObjectId.get().toString();
        repository.save(entity(BOB, bobId));

        assertThat(collection.findOne().get("_id"), is(bobId));
        assertThat(collection.count(), is(1L));
    }
}
