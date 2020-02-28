package com.phenjuly.keycloak.spi.authenticator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PhenJuly
 */
@Data
@NoArgsConstructor
public class User {

    private String userId;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public User(String userId, String userName, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = this.userName + "@qq.com";
        this.password = "7777777";
    }
}
