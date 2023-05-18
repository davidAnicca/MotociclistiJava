package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.out.Operations;
import org.example.services.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class Main {

    private final static Logger log = LogManager.getLogger();
    private static Properties props;

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }


    public static void main(String[] args) throws IOException {
        log.debug("test debug");
        props = new Properties();
        try {
            log.error("test - this is not an error!");
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            log.error("Cannot find bd.config " + e);
        }
        log.error("test - this is not an error!");
        SpringApplication.run(Main.class, args);
        // Operations server = new Operations(
        //        new Service(props)
        //);

    }
}