package org.example.dao;

import org.example.parser.Parser;
import org.example.writer.Writer;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @param <C> - container, that contains list of target objects
 * @param <T> - target object generic
 */
public abstract class AbstractXMLDao<C, T extends IdIterable> implements GenericCrudDao<T>{
    protected final String inputFileXML;
    protected final Parser<C> parser;
    protected final Writer<C> writer;
    public AbstractXMLDao(String inputFileXML, Parser<C> parser, Writer<C> writer) {
        this.inputFileXML = inputFileXML;
        this.parser = parser;
        this.writer = writer;
    }
    protected void writeFile(C parsed) {
        writer.write(inputFileXML, parsed);
    }

    protected C readFile() {
        return parser.parse(inputFileXML);
    }

    protected abstract List<T> toCollection(C container);

    protected  <V> V withReadWrite(Function<C, V> function) {
        C container = readFile();
        V result =  function.apply(container);
        writeFile(container);
        return result;
    }

    private void withReadWrite(Consumer<C> consumer) {
        C container = readFile();
        consumer.accept(container);
        writeFile(container);
    }
    @Override
    public List<T> findAll() {
        List<T> list = toCollection(readFile());
        list.sort((o1, o2) ->(int) (o1.getId() - o2.getId()));
        return list;
    }

    @Override
    public T create(T object) {
        return withReadWrite(parsed->{
            List<T> departmentList = toCollection(parsed);
            if (object.getId()==null) {
                Long nextId = findNextId(departmentList);
                object.setId(nextId);
            }
            departmentList.add(object);
            return object;
        });
    }

    private Long findNextId(List<T> objectList) {
        OptionalLong max = objectList.stream().mapToLong(IdIterable::getId).max();
        if (max.isEmpty()) {
            return 1L;
        }
        return max.getAsLong()+1L;
    }

    @Override
    public void update(T object) {
        withReadWrite(parsed -> {
            List<T> objectList = toCollection(parsed);
            Long targetId = object.getId();
            if (targetId==null){
                throw new IllegalArgumentException("Not initialized department passed to update");
            }
            deleteById(objectList, targetId);
            objectList.add(object);
        });
    }

    private void deleteById(List<T> objectList, Long targetId) {
        if(!objectList.removeIf(d-> Objects.equals(d.getId(), targetId))){
            throw new NoSuchElementException("Called to remove not existing department");
        }
    }

    @Override
    public void delete(T object) {
        withReadWrite(container -> {
            if (!toCollection(container).remove(object)) {
                throw new NoSuchElementException("Called to not existing department");
            }
        });

    }

    @Override
    public boolean delete(Long id) {
        return withReadWrite(container -> {
            try {
                deleteById(toCollection(container), id);
            } catch (NoSuchElementException e) {
                return false;
            }
            return true;
        });
    }

    @Override
    public Optional<T> find(Long id) {
        return findAll().stream().filter(d->id.equals(d.getId())).findFirst();
    }
}
