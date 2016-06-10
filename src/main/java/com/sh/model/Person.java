package com.sh.model;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author shoe011
 * DTO class for transport data between reader to processor and finally to writer.<br>
 * XmlRootElement is necessary if want to map it with JAXB from XML
 *
 */
@XmlRootElement(name="person")
public class Person {
    private String lastName;
    private String firstName;

    public Person() {

    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "firstName: " + firstName + ", lastName: " + lastName;
    }

}
