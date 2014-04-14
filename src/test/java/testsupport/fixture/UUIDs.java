package testsupport.fixture;

import java.util.UUID;

public class UUIDs {

    public static UUID predictableUUID() {
        return UUID.nameUUIDFromBytes("predictableUUID".getBytes());
    }

    public static UUID randomUUID() {
        return UUID.randomUUID();
    }
}
