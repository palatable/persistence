package testsupport.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    public static final Item A = item("A");
    public static final Item B = item("B");
    public static final Item C = item("C");

    private final String label;

    private Item(@JsonProperty("label") String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Item) {
            Item that = (Item) other;
            return this.getLabel().equals(that.getLabel());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }


    @Override
    public String toString() {
        return "Item{" +
                "label='" + label + '\'' +
                '}';
    }

    public static Item item(String label) {
        return new Item(label);
    }
}
