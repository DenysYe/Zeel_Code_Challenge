package com.zeel.tests;

import com.zeel.utilities.ConfigurationReader;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.baseURI;


public class ZeelTestBase {
    @BeforeAll
    public static void setUp() {
        baseURI = ConfigurationReader.getProperty("patience.url");

    }
}
