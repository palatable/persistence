package testsupport.domain;

import lombok.Value;

import static testsupport.domain.Gender.FEMALE;
import static testsupport.domain.Gender.MALE;

@Value(staticConstructor = "person")
public class Person {

    String firstName;
    Gender gender;

    public static Person male(String firstName) {
        return person(firstName, MALE);
    }

    public static Person female(String firstName) {
        return person(firstName, FEMALE);
    }
}
