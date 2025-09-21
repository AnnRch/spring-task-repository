package org.example.notused;

import org.example.model.impl.Trainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TrainerLoader {

    private Map<String, Trainer> map = new ConcurrentHashMap<>();
    private int counter = 0;


    public Map<String, Trainer> loadDataFromJson(){
        return Map.of();
    }
}
