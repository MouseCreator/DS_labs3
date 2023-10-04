package org.example.filebase.model;

public class PersonParser implements Parser<Person> {
    @Override
    public Person fromString(String s) {
        String[] namePhonePair = s.split("-");
        assert namePhonePair.length == 2;

        Person person = new Person();

        String name = namePhonePair[0];
        person.setName(name.trim());

        String phone = namePhonePair[1];
        person.setPhone(phone.trim());

        return person;
    }

    @Override
    public String toString(Person instance) {
        return instance.getName() + " - " + instance.getPhone();
    }
}
