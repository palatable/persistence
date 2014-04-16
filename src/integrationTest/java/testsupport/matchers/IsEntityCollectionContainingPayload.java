package testsupport.matchers;

import com.jnape.palatable.persistence.Entity;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;

import java.util.ArrayList;
import java.util.List;

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
    @SuppressWarnings("Convert2Diamond")
    public static <Payload, Id> Matcher<Iterable<Entity<Payload, Id>>> hasPayloads(Payload... payloads) {
        List<Matcher<? super Iterable<Entity<Payload, Id>>>> all = new ArrayList<>();
        for (Payload payload : payloads)
            all.add(new IsEntityCollectionContainingPayload<>(payload));

        return allOf(new IsIterableOfLengthMatcher<Payload, Id>(payloads), allOf(all));
    }

    public static <Payload, Id> IsEntityCollectionContainingPayload<Payload, Id> hasPayload(Payload payload) {
        return new IsEntityCollectionContainingPayload<>(payload);
    }

}
