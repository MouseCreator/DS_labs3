package org.example.filebase.model;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Person> fromStringMulti(String data) {
        String[] split = data.split("\n");
        List<Person> personList = new ArrayList<>();
        for (String s : split) {
            if (s.trim().isEmpty()) {
                continue;
            }
            personList.add(fromString(s));
        }
        return personList;
    }

    @Override
    public String toStringMulti(List<Person> instances) {
        StringBuilder builder = new StringBuilder();
        for (Person p : instances) {
            builder.append(toString(p)).append('\n');
        }
        return builder.toString();
    }
}
