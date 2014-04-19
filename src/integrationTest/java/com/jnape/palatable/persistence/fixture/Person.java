package com.jnape.palatable.persistence.fixture;

import lombok.Value;

import static com.jnape.palatable.persistence.fixture.Gender.FEMALE;
import static com.jnape.palatable.persistence.fixture.Gender.MALE;

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
