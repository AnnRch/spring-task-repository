package org.example.notused;

import org.example.model.impl.Trainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TrainerStorage {

    private Map<String, Trainer> storage = new ConcurrentHashMap<>();

    private boolean isPresent(String firstName, String lastName){
        for(Map.Entry<String, Trainer> entry : storage.entrySet()){
            if(entry.getValue().getFirstName().equals(firstName) &&
                    entry.getValue().getLastName().equals(lastName)
            ){
                return true;
            }
        }

        return false;
    }

    private Long genrateId(){
        return 0L;
    }

    private String generateUserName(String firstName, String lastName){
            return isPresent(firstName, lastName)
                    ? firstName + "." + lastName
                    : firstName + "." + lastName + genrateId();
    }

    public Trainer addToStorage(Trainer trainer){
        return null;
    }

    public Trainer update(Trainer trainer){
        return null;
    }

    public void deleteFromStorage(String key){

    }
}
