package testsupport.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnape.palatable.persistence.mongo.serialization.BidirectionalJsonSerializer;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import testsupport.domain.Person;
import testsupport.fixture.ObjectMappers;

public class DBObjectConverter {

    public static DBObject convertToDBObject(Person person) {
        ObjectMapper objectMapper = ObjectMappers.configuredForTestFixtures();
        BidirectionalJsonSerializer serializer = new BidirectionalJsonSerializer(objectMapper);
        return (DBObject) JSON.parse(serializer.serialize(person));
    }
}
