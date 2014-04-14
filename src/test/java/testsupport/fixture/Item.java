package testsupport.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Item {
    @Getter private final String label;

    private Item(@JsonProperty("label") String label) {
        this.label = label;
    }

    public static Item item(String label) {
        return new Item(label);
    }

    public static final Item A = item("A");
    public static final Item B = item("B");
    public static final Item C = item("C");

    public static final Object PROBLEM_CHILD = new Object();
}
