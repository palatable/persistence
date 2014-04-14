package com.jnape.palatable.persistence;

import org.junit.Test;
import testsupport.fixture.Item;
import testsupport.fixture.UUIDs;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static testsupport.fixture.Item.*;

public class EntityTest {

    @Test
    public void twoEntitiesWithTheSameTypeAndIdAreEqual() {
        Entity<Item, UUID> entity = new Entity<>(A, UUIDs.predictableUUID());
        Entity<Item, UUID> sameEntity = new Entity<>(B, UUIDs.predictableUUID());

        assertThat(entity, is(sameEntity));
    }

    @Test
    public void twoEntitiesWithTheSameTypeButDifferentIdsAreNotEqual() {
        Entity<Item, UUID> entity = new Entity<>(A, UUIDs.predictableUUID());
        Entity<Item, UUID> differentEntity = new Entity<>(A, UUIDs.randomUUID());

        assertThat(entity, is(not(differentEntity)));
    }

    @Test
    public void twoEntitiesWithTheSameIdButDifferentTypesAreNotEqual() {
        Entity<Item, UUID> entity = new Entity<>(C, UUIDs.predictableUUID());
        Entity<String, UUID> differentEntity = new Entity<>("Not an item", UUIDs.predictableUUID());

        assertThat(entity, is(not((Entity) differentEntity)));
    }
}
