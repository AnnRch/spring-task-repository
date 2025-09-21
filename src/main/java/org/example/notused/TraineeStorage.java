package org.example.notused;

import org.example.model.impl.Trainee;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TraineeStorage {

    private Map<String, Trainee> storage = new ConcurrentHashMap<>();


    public Trainee addToStorage(Trainee trainee){
        return null;
    }

    public Trainee update(Trainee trainee){
        return null;
    }

    public void deleteFromStorage(String key){

    }
}
