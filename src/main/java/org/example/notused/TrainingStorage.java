package org.example.notused;

import org.example.model.impl.Training;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TrainingStorage {

    private Map<String, Training> storage = new ConcurrentHashMap<>();


    public Training addToStorage(Training training){
        return null;
    }

    public Training update(Training training){
        return null;
    }

    public void deleteFromStorage(String key){

    }
}
