package org.example.service;

import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
    Training getById(Long id);
    List<Training> getAll();
    List<Training> getAllByDate(LocalDate date);
    Training create(Training training);
    Training update(Training training);
    Training createTrainingByTrainer(Trainer trainer, Training training);
    Training enrollForTraining(Trainee trainee, Training training);
    Training withdrawFromTraining(Trainee trainee, Training training);
    Training removeTrainingFromTrainer(Trainer trainer, Training training);
    void deleteById(Long id);
}
