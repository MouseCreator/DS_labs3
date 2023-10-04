package org.example.filebase.model;

import org.example.filebase.generator.DataGenerator;

public class PersonFactory implements Factory<Person> {
    private final DataGenerator dataGenerator;

    public PersonFactory(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    @Override
    public Person get() {
        Person person = new Person();
        person.setName(dataGenerator.generateName());
        person.setPhone(dataGenerator.generatePhone());
        return person;
    }
}
