package testsupport.matchers;

import com.jnape.palatable.persistence.Entity;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static java.lang.String.format;

class IsIterableOfLengthMatcher<Payload, Id> extends BaseMatcher<Iterable<Entity<Payload, Id>>> {
    private final Payload[] payloads;

    @SafeVarargs
    public IsIterableOfLengthMatcher(Payload... payloads) {
        this.payloads = payloads;
    }

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
}
