package com.jnape.palatable.persistence;

import org.junit.Test;
import testsupport.fixture.Item;

import java.util.UUID;

import static com.jnape.palatable.persistence.Entity.entity;
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
        Entity<Item, String> entity = entity(A, id);
        Entity<Item, String> updated = entity.update(p -> B);

        assertThat(updated.get(), is(B));
        assertThat(updated.id(), is(id));
    }
}
