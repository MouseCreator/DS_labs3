package org.example.dao;

import org.example.extra.TestHelper;
import org.example.manager.FileManager;
import org.example.parser.Parser;
import org.example.extra.Paths;
import org.example.writer.Writer;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractCrudDaoTest<C, T extends IdIterable> {
    protected static final String TEST_XML = Paths.TEST_DEPARTMENTS_TEMP;
    protected static final String ORIGIN_XML = Paths.TEST_DEPARTMENTS_XML;
    protected Parser<C> parser;
    protected Writer<C> writer;
    protected FileManager fileManager;
    protected GenericCrudDao<T> crudDao;

    protected void refresh() {
        fileManager.copyXML(ORIGIN_XML, TEST_XML);
    }
    protected void initialize(Parser<C> parser, Writer<C> writer, FileManager fileManager, GenericCrudDao<T> dao) {
        this.parser = parser;
        this.writer = writer;
        this.fileManager = fileManager;
        this.crudDao = dao;
    }
    protected void initializeAndRefresh(Parser<C> parser, Writer<C> writer, FileManager fileManager, GenericCrudDao<T> dao) {
        initialize(parser, writer, fileManager, dao);
        refresh();
    }
    protected abstract T newInstance();
    protected abstract List<T> expectedData();
    void findAll() {
        List<T> expectedList = expectedData();
        List<T> actualList = crudDao.findAll();
        TestHelper.compareList(expectedList, actualList);
    }

    void create() {
        List<T> expectedList = expectedData();
        long next = expectedList.size()+1;
        expectedList.add(newInstance());
        T noIdInstance = newInstance();
        noIdInstance.setId(null);
        T updatedDepartment = crudDao.create(noIdInstance);
        assertEquals(next, updatedDepartment.getId());
        List<T> updatedList = crudDao.findAll();
        TestHelper.compareList(expectedList, updatedList);
    }


    void update() {
        T updated = newInstance();
        updated.setId(1L);
        crudDao.update(updated);
        Optional<T> optionalT = crudDao.find(1L);
        assertTrue(optionalT.isPresent());
        assertEquals(updated, optionalT.get());
    }

    void delete() {
        Optional<T> departmentOptional = crudDao.find(1L);
        int initialSize = crudDao.findAll().size();
        assertTrue(departmentOptional.isPresent());
        T obj = departmentOptional.get();
        crudDao.delete(obj);
        assertThrows(NoSuchElementException.class, () -> crudDao.delete(obj));
        assertTrue(crudDao.find(1L).isEmpty());
        int finalSize = crudDao.findAll().size();
        assertEquals(initialSize-1, finalSize);
    }

    void testDelete() {
        Optional<T> optionalT = crudDao.find(1L);
        int initialSize = crudDao.findAll().size();
        assertTrue(optionalT.isPresent());
        assertTrue(crudDao.delete(1L));
        assertFalse(() -> crudDao.delete(1L));
        assertTrue(crudDao.find(1L).isEmpty());
        int finalSize = crudDao.findAll().size();
        assertEquals(initialSize-1, finalSize);
    }

    void find() {
        List<T> departmentList = expectedData();
        long size = departmentList.size();
        for (int i = 1; i <= size; i++) {
            Optional<T> departmentOptional = crudDao.find((long) i);
            assertTrue(departmentOptional.isPresent());
            assertEquals(departmentOptional.get(), departmentList.get(i-1));
        }
        assertTrue(crudDao.find(size+1).isEmpty());
    }
}