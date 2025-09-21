package org.example.service;

import org.example.model.impl.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer getById(Long id);
    List<Trainer> getAll();
    Trainer create(Trainer trainer);
    Trainer update(Trainer trainer);
    void deleteById(Long id);
    void addTrainingToTrainer(Trainer trainer, Long trainingId);
}
