package testsupport.matchers;

import com.jnape.palatable.persistence.Entity;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.core.AllOf.allOf;

public class IsEntityCollectionContainingPayload<Payload, Id> extends IsCollectionContaining<Entity<Payload, Id>> {

    public IsEntityCollectionContainingPayload(Payload payload) {
        super(new BaseMatcher<Entity<Payload, Id>>() {
            @Override
            public boolean matches(Object item) {
                return item instanceof Entity && payload.equals(((Entity) item).get());
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(payload);
            }
        });
    }

    @SafeVarargs
    public static <Payload, Id> Matcher<Iterable<Entity<Payload, Id>>> hasPayloads(Payload... payloads) {
        List<Matcher<? super Iterable<Entity<Payload, Id>>>> all = new ArrayList<>();
        for (Payload payload : payloads)
            all.add(new IsEntityCollectionContainingPayload<>(payload));

        return allOf(new BaseMatcher<Iterable<Entity<Payload, Id>>>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof Iterable && sizeOf((Iterable) o) == payloads.length;
            }

            private int sizeOf(Iterable iterable) {
                int size = 0;
                for (Object ignored : iterable)
                    size++;
                return size;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(format("an iterable expecting <%s> elements", payloads.length));
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText(format("actually had <%s> elements", sizeOf((Iterable) item)));
            }
        }, allOf(all));
    }

    public static <Payload, Id> IsEntityCollectionContainingPayload<Payload, Id> hasPayload(Payload payload) {
        return new IsEntityCollectionContainingPayload<>(payload);
    }
}
