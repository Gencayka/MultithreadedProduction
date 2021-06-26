package ru.chayka.mproduction.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileParser {
    private static final Logger log = LoggerFactory.getLogger(PropertiesFileParser.class.getName());

    public static Properties parse(String fileName, Class<?> mainClass) throws Exception {
        try (InputStream fileStream = mainClass.getClassLoader().getResourceAsStream(fileName)) {
            Properties properties = new Properties();
            properties.load(fileStream);
            return properties;
        } catch (Exception ex) {
            log.error("An exception occured during parsing the {} file", fileName);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
