package com.jfarrelly.intellij.plugin.code.inspection;

import java.util.Arrays;

class Examples {
  private String getFullName(String firstName, String lastName) {
    return firstName + " " + lastName;
  }

  private String getLegalName(String firstName, String middleName, String... lastNames) {
    return firstName + " " + middleName + " " + Arrays.toString(lastNames);
  }

  private void checkVariables() {
    String firstName = "IntelliJ";
    String middleName = "JetBrains";
    String lastName = "IDEA";

    // NO PROBLEMS: Parameters in correct order
    getFullName(firstName, lastName);
    getFullName(firstName, firstName);
    getFullName(lastName, lastName);
    getLegalName(firstName, middleName);
    getLegalName(firstName, middleName, lastName);

    // PROBLEMS: Parameters in wrong order
    getFullName(lastName, firstName);
    getLegalName(middleName, firstName);
    getLegalName(firstName, lastName, middleName);
  }

  private void checkMethods() {
    // NO PROBLEMS: Parameters in correct order
    getFullName(getFirstName(), getLastName());
    getFullName(getFirstName(), getFirstName());
    getFullName(getLastName(), getLastName());

    Person person = new Person(getFirstName(), getLastName());
    getFullName(person.getFirstName(), person.getLastName());
    getFullName(person.getFirstName(), person.getFirstName());
    getFullName(person.getLastName(), person.getLastName());

    // PROBLEMS: Parameters in wrong order
    getFullName(getLastName(), getFirstName());
    getFullName(person.getLastName(), person.getFirstName());
  }

  private String getFirstName() {
    return "IntelliJ";
  }

  private String getLastName() {
    return "IDEA";
  }
}
