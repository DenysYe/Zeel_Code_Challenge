package com.zeel.tests;

import com.google.gson.Gson;
import com.zeel.pojos.Patient;
import com.zeel.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PatientTests extends ZeelTestBase {


   private final Logger logger = LogManager.getLogger(PatientTests.class);
    @Test
    public void appointmentDateVerification() {
        Response response = given().accept(ContentType.JSON)
                .when().get("/patient");
        logger.info("status code is " + response.statusCode());
        assertEquals(200, response.statusCode());
        logger.info("content type is " + response.contentType());
        assertEquals("application/json", response.contentType());
        List<Map<String, ?>> patientList = response.as(List.class);
        int countOfJunePatients = 0;
        List<String> dates = new ArrayList<>();
        for (Map<String, ?> eachPatient : patientList) {
            if (eachPatient.get("appointment_date").toString().contains("2022-06")) {
                countOfJunePatients++;
                dates.add(eachPatient.get("appointment_date").toString());
            }
        }
        if (countOfJunePatients >= 1) {
            logger.info("Total number of patients with appointment in June 2022(2022-06) is:  " + countOfJunePatients);
            logger.info("Dates for their appointments are : " + dates);
        } else {
            logger.info("There are no patients for this date");
        }
    }

    @Test
    public void checkIfPatientExists() throws FileNotFoundException {
        Response response = given().accept(ContentType.JSON)
                .when().get("/patient");
        logger.info("status code is " + response.statusCode());
        assertEquals(200, response.statusCode());
        logger.info("content type is " + response.contentType());
        assertEquals("application/json", response.contentType());
        List<Map<String, ?>> patientList = response.as(List.class);
        FileReader reader = new FileReader("src/test/resources/patient.json");
        Gson gson = new Gson();
        Map<String, String> myPatient = gson.fromJson(reader, Map.class);
        boolean patientIsPresent = false;
        for (Map<String, ?> eachPatient : patientList) {
            if (eachPatient.equals(myPatient)) {
                patientIsPresent = true;
                break;
            }
        }
        Assertions.assertTrue(patientIsPresent);
    }

    @Test
    public void VerifyIDFormat() {
        Response response = given().accept(ContentType.JSON)
                .when().get("/patient");
        logger.info("status code is " + response.statusCode());
        assertEquals(200, response.statusCode());
        logger.info("content type is " + response.contentType());
        assertEquals("application/json", response.contentType());
        List<Map<String, ?>> patientList = response.as(List.class);
        String customID = "";
        List<Patient> allPatients = response.jsonPath().getList("", Patient.class);
        for (Patient eachPatient : allPatients) {
            String firstInitial = eachPatient.getName().getFirstName().substring(0, 1);
            String lastInitial = eachPatient.getName().getLastName().substring(0, 1);
            String yearBirth = eachPatient.getBirthdate().substring(0, 4);
            String monthBirth = eachPatient.getBirthdate().substring(5, 7);
            String dayBirth = eachPatient.getBirthdate().substring(8, 10);
            String yearAppointment = eachPatient.getAppointment_date().substring(0, 4);
            String monthAppointment = eachPatient.getAppointment_date().substring(5, 7);
            String dayAppointment = eachPatient.getAppointment_date().substring(8, 10);
            customID =  firstInitial+ lastInitial + yearBirth + monthBirth + dayBirth + yearAppointment + monthAppointment + dayAppointment;
            Assertions.assertTrue(eachPatient.getId().contains(customID));
            Assertions.assertTrue(eachPatient.getId().length() == 22);
        }
    }

    @Test
    public void patchDataComparing() throws FileNotFoundException {
        Response response = given().accept(ContentType.JSON)
                .when().get("/patient");
        logger.info("status code is " + response.statusCode());
        assertEquals(200, response.statusCode());
        logger.info("content type is " + response.contentType());
        assertEquals("application/json", response.contentType());
        String expected_id = "SR19760827202206208364";
        Patient originalPatient= new Patient();
        List<Patient> allPatients = response.jsonPath().getList("", Patient.class);
        for (Patient eachPatient : allPatients) {
            if (eachPatient.getId().equals(expected_id)) {
                originalPatient = eachPatient;
            }
        }
        FileReader reader = new FileReader("src/test/resources/updatedPatient.json");
        Gson gson = new Gson();
        Map<String, String> patientMap = gson.fromJson(reader, Map.class);
        ValidatableResponse validatableResponse = given().contentType(ContentType.JSON)
                .and().queryParam("api_key", ConfigurationReader.getProperty("api_key"))
                .and().body(patientMap)
                .when().patch("/update")
                .then().assertThat().statusCode(200);
        validatableResponse.body("id", is(originalPatient.getId()));
        validatableResponse.body("name.firstName", is("Awesome"));
        validatableResponse.body("name.lastName", is("Tester"));
        validatableResponse.body("address.street", is("123 Ukr drive"));
        validatableResponse.body("address.city", is("Kyiv"));
        validatableResponse.body("address.state", is("UA"));
        validatableResponse.body("address.zip", is("12345"));
        validatableResponse.body("phone", is(originalPatient.getPhone()));
        validatableResponse.body("birthdate", is(originalPatient.getBirthdate()));
        validatableResponse.body("appointment_date", is(originalPatient.getAppointment_date()));
    }
}



