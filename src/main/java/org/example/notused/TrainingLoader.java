package org.example.notused;

import org.example.model.impl.Training;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TrainingLoader {

    private Map<String, Training> map = new ConcurrentHashMap<>();
    private int counter = 0;


    public Map<String, Training> loadDataFromJson(){
        return Map.of();
    }
}
