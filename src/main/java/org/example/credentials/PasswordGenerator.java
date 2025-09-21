package org.example.credentials;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PasswordGenerator {

    public String generatePassword(){
        String template = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
        StringBuffer password = new StringBuffer();
        int counter = 0;
        Random random = new Random();

        while(counter < 10){
            int index = random.nextInt(template.length());
            password.append(template.charAt(index));
            counter++;
        }

        return password.toString();
    }
}
