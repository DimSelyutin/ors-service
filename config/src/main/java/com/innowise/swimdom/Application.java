package com.innowise.swimdom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main class of application.
 */
@Slf4j
@EnableJpaAuditing
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Integer a = 9999;

        System.out.println(a);

        changeList(a);
        System.out.println(a);
        // SpringApplication.run(Application.class, args);
    }

    public static void changeList(Integer c) {
        System.out.println(c);
        c = 10;

    }
}
