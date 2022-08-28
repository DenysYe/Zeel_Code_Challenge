package com.zeel.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Patient {
    private String id;
    private String birthdate;
    private String phone;
    private String appointment_date;
    private Name name;
    private Address address;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    @JsonProperty
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAppointment_date() {
        return appointment_date;
    }
    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }
    public Name getName() {
        return name;
    }
    public void setName(Name name) {
        this.name = name;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public static class Name{
        private String lastName;
        private String firstName;
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }
    public static class Address{
        private String street;
        private String city;
        private String state;
        private String zip;
        public String getStreet() {
            return street;
        }
        public void setStreet(String street) {
            this.street = street;
        }
        public String getCity() {
            return city;
        }
        public void setCity(String city) {
            this.city = city;
        }
        public String getState() {
            return state;
        }
        public void setState(String state) {
            this.state = state;
        }
        public String getZip() {
            return zip;
        }
        public void setZip(String zip) {
            this.zip = zip;
        }
    }



}
