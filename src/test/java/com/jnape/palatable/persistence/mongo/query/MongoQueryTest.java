package com.jnape.palatable.persistence.mongo.query;

import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MongoQueryTest {

    @Test
    public void queryStartsWithEmptyCriteria() {
        assertThat(MongoQuery.query().getCriteria(), is(new BasicDBObject()));
    }

    @Test
    public void eqAddsEqualityCriteria() {
        assertThat(
                MongoQuery.query().eq("key", "value").getCriteria(),
                is(new BasicDBObject("key", "value"))
        );
    }

    @Test
    public void $existsAddsExistenceCriteria() {
        assertThat(
                MongoQuery.query().$exists("key", true).getCriteria(),
                is(new BasicDBObject("key", new BasicDBObject("$exists", true)))
        );

    } 
    
    @Test
    public void accumulatesCriterion() {
        assertThat(
                MongoQuery.query()
                        .eq("key", "value")
                        .eq("key2", "value2")
                        .getCriteria(),
                is(new BasicDBObject() {{
                    put("key", "value");
                    put("key2", "value2");
                }})
        );
    }
}
