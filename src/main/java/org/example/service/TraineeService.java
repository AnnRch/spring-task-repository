package org.example.service;

import org.example.model.impl.Trainee;

import java.util.List;

public interface TraineeService {

    Trainee getById(Long id);
    List<Trainee> getAll();
    Trainee create(Trainee trainee);
    Trainee update(Trainee trainee);
    void deleteById(Long id);
    void addTrainingToTrainee(Trainee trainee, Long trainingId);
}
