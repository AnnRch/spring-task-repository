package org.example.notused;

import org.example.model.impl.Trainee;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TraineeLoader {

    private Map<String, Trainee> map = new ConcurrentHashMap<>();
    private int counter = 0;


    public Map<String, Trainee> loadDataFromJson(){
        return Map.of();
    }
}
