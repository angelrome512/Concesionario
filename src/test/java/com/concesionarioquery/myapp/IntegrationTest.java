package com.concesionarioquery.myapp;

import com.concesionarioquery.myapp.ConcesionarioqueryApp;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = ConcesionarioqueryApp.class)
public @interface IntegrationTest {
}
