package com.jnape.palatable.persistence;

import java.util.function.Function;

public final class Entity<Payload, Id> {

    private final Payload payload;
    private final Id      id;

    private Entity(Payload payload, Id id) {
        this.payload = payload;
        this.id = id;
    }

    public Payload get() {
        return payload;
    }

    public Id id() {
        return id;
    }

    public <Update> Entity<Update, Id> update(Function<Payload, Update> fn) {
        return entity(fn.apply(get()), id());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Entity) {
            Entity that = (Entity) other;

            boolean sameType = this.get().getClass().equals(that.get().getClass());
            boolean sameId = this.id().equals(that.id());

            return sameType && sameId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = payload.getClass().hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "payload=" + payload +
                ", id=" + id +
                '}';
    }

    public static <Payload, Id> Entity<Payload, Id> entity(Payload payload, Id id) {
        return new Entity<>(payload, id);
    }
}
