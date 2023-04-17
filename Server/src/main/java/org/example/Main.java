package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {

    private final static Logger log = LogManager.getLogger();
    private static Properties props;

    public static void main(String[] args) {
        log.debug("test debug");
        props = new Properties();
        try {
            log.error("test - this is not an error!");
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            log.error("Cannot find bd.config " + e);
        }
        log.error("test - this is not an error!");
    }
}