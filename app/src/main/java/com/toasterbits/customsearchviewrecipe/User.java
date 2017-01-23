package com.toasterbits.customsearchviewrecipe;

import com.orm.SugarRecord;

/**
 * Created by chrisjeon on 1/23/17.
 */

public class User extends SugarRecord {
    public String firstName;
    public String lastName;
    public String email;

    // Empty constructor for SugarORM
    public User() {}

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
