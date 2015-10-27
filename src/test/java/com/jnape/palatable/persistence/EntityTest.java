package com.jnape.palatable.persistence;

import com.jnape.palatable.persistence.fixture.Person;
import org.junit.Test;
import testsupport.fixture.Item;

import java.util.UUID;

import static com.jnape.palatable.persistence.Entity.entity;
import static com.jnape.palatable.persistence.fixture.Person.female;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static testsupport.fixture.Item.A;
import static testsupport.fixture.Item.B;
import static testsupport.fixture.Item.C;
import static testsupport.fixture.UUIDs.predictableUUID;
import static testsupport.fixture.UUIDs.randomUUID;

public class EntityTest {

    @Test
    public void twoEntitiesWithTheSameTypeAndIdAreEqual() {
        Entity<Item, UUID> entity = entity(A, predictableUUID());
        Entity<Item, UUID> sameEntity = entity(B, predictableUUID());

        assertThat(entity, is(sameEntity));
    }

    @Test
    public void twoEntitiesWithTheSameTypeButDifferentIdsAreNotEqual() {
        Entity<Item, UUID> entity = entity(A, predictableUUID());
        Entity<Item, UUID> differentEntity = entity(A, randomUUID());

        assertThat(entity, is(not(differentEntity)));
    }

    @Test
    public void twoEntitiesWithTheSameIdButDifferentTypesAreNotEqual() {
        Entity<Item, UUID> entity = entity(C, predictableUUID());
        Entity<String, UUID> differentEntity = entity("Not an item", predictableUUID());

        assertThat(entity, is(not(differentEntity)));
    }

    @Test
    public void updateTransformsPayloadRetainingId() {
        String id = "id";
        Entity<Person, String> karen = entity(female("Karen"), id);
        Entity<Person, String> julie = karen.update(p -> female("Julie"));

        assertThat(julie.get().getFirstName(), is("Julie"));
        assertThat(julie.id(), is(id));
    }
}
