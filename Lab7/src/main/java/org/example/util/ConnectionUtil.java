package org.example.util;

import org.example.configuration.ConfigurationParser;
import org.example.configuration.CustomConfiguration;
import org.example.validator.Validator;
import org.example.validator.XSDValidator;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static boolean CONFIGURED = false;
    private static String DB_URL;
    private static final String CONFIG_XML= "src/main/resources/XML/configuration.xml";
    private static final String CONFIG_XSD = "src/main/resources/XSD/configuration.xsd";
    private static Properties DB_PROPERTIES;

    private static void configure() {
        CustomConfiguration configuration = getConfiguration();
        DB_URL = configuration.getUrl();
        DB_PROPERTIES = new Properties();
        DB_PROPERTIES.put("user", configuration.getUser());
        DB_PROPERTIES.put("password", configuration.getPassword());
        DB_PROPERTIES.put("sqlDialect", "mysql");
    }

    private static CustomConfiguration getConfiguration() {
        Validator validator = new XSDValidator();
        try {
            validator.validate(CONFIG_XSD, CONFIG_XML);
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Given Configuration XML is not valid!", e);
        }
        ConfigurationParser parser = new ConfigurationParser();
        return parser.parseXmlFile(CONFIG_XML);
    }

    public static Connection getConnection() {
        if (!CONFIGURED) {
            configure();
            CONFIGURED = true;
        }

        try {
            return DriverManager.getConnection(DB_URL, DB_PROPERTIES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
