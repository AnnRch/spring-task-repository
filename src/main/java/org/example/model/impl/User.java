package org.example.model.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class User {

    @Setter
    protected String firstName;

    @Setter
    protected String lastName;

    @Setter
    protected String userName;

    @Setter
    protected String password;

    @Setter
    protected boolean isActive;


    public User(String firstName, String lastName, String userName, String password, boolean isActive){
        this.lastName = lastName;
        this.firstName = firstName;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
    }
}
