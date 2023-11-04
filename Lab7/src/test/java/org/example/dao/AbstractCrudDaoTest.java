package org.example.dao;

import org.example.manager.FileManager;
import org.example.manager.FileManagerImpl;
import org.example.model.Departments;
import org.example.parser.DepartmentStaxParser;
import org.example.parser.Parser;
import org.example.paths.Paths;
import org.example.writer.DepartmentsWriter;
import org.example.writer.Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCrudDaoTest {
    private static final String TEST_XML = Paths.TEST_DEPARTMENTS_TEMP;
    private static final String ORIGIN_XML = Paths.TEST_DEPARTMENTS_XML;
    private Parser<Departments> parser;
    private Writer<Departments> writer;

    private FileManager fileManager;

    private void refresh() {
        fileManager.copyXML(ORIGIN_XML, TEST_XML);
    }

    @BeforeEach
    void setUp() {
        parser = new DepartmentStaxParser();
        writer = new DepartmentsWriter();
        fileManager = new FileManagerImpl();
        refresh();
    }
    private XMLDepartmentsDao createDepartmentsDao() {
        if (writer==null) {
            throw new IllegalStateException("Writer is not initialized");
        }
        if (parser==null){
            throw new IllegalStateException("Parser is not initialized");
        }
        return new XMLDepartmentsDao(TEST_XML, parser, writer);
    }

    @Test
    void findAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void find() {
    }
}