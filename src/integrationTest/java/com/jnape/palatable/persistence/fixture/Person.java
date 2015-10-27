package com.jnape.palatable.persistence.fixture;


import static com.jnape.palatable.persistence.fixture.Gender.FEMALE;
import static com.jnape.palatable.persistence.fixture.Gender.MALE;

public class Person {

    private final String firstName;
    private final Gender gender;

    private Person(String firstName, Gender gender) {
        this.firstName = firstName;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Person) {
            Person that = (Person) other;

            boolean sameFirstName = this.getFirstName().equals(that.getFirstName());
            boolean sameGender = this.getGender().equals(that.getGender());

            return sameFirstName && sameGender;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 31 * firstName.hashCode() + gender.hashCode();
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", gender=" + gender +
                '}';
    }

    public static Person person(String firstName, Gender gender) {
        return new Person(firstName, gender);
    }

    public static Person male(String firstName) {
        return person(firstName, MALE);
    }

    public static Person female(String firstName) {
        return person(firstName, FEMALE);
    }
}
