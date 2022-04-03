package org.example;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeAll;

public class LogMain {

    @BeforeAll
    static void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


}