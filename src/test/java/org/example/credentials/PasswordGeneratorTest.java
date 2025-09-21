package org.example.credentials;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {

    private final PasswordGenerator passwordGenerator = new PasswordGenerator();

    @Test
    void generatePasswordTest(){

        String password = passwordGenerator.generatePassword();

        Assertions.assertNotNull(password);
        Assertions.assertEquals(10, password.length());

        String template = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";

        for(int i = 0; i < 10; i++){
            char ch = password.charAt(i);
            Assertions.assertTrue(template.contains(String.valueOf(ch)));
        }
    }
}
