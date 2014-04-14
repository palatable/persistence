package testsupport.fixture;

import testsupport.domain.Person;

import static testsupport.domain.Person.female;
import static testsupport.domain.Person.male;

public class People {
    public static final Person ALICE   = female("Alice");
    public static final Person BOB     = male("Bob");
    public static final Person CHELSEA = female("Chelsea");
    public static final Person DAVID   = male("David");
    public static final Person ELEANOR = female("Eleanor");
    public static final Person ETHAN   = male("Ethan");
}
