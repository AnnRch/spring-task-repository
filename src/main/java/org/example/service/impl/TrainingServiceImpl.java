package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.impl.TrainingDAO;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private final TrainingDAO trainingDAO;

    private final TrainerService trainerService;
    private final TraineeService traineeService;

    @Override
    public Training getById(Long id) {
        if(id == null){
            throw new NullPointerException("null value");
        }

        return trainingDAO.getById(id);
    }

    @Override
    public List<Training> getAll() {
        return trainingDAO.getAll();
    }

    @Override
    public List<Training> getAllByDate(LocalDate date) {
        return trainingDAO.getAll().stream()
                .filter(training -> training.getDate().equals(date))
                .toList();
    }

    @Override
    public Training create(Training training) {

        if(training == null){
            throw new NullPointerException("null training value");
        }
        return trainingDAO.create(training);
    }

    @Override
    public Training update(Training training) {
        if(training == null){
            throw new NullPointerException("null training value");
        }

        return trainingDAO.update(training);
    }

    @Override
    public Training createTrainingByTrainer(Trainer trainer, Training training) {

        if(trainer.getTrainingsIds().contains(training.getId())){
            throw new IllegalArgumentException("trainer has already assigned for this training");
        }

        training.setTrainerId(trainer.getUserId());
        trainerService.addTrainingToTrainer(trainer, training.getId());

        return update(training);
    }

    @Override
    public Training enrollForTraining(Trainee trainee, Training training) {

        if(trainee == null || training == null){
            throw new NullPointerException("null values were given");
        }

       if(training.getTraineeIds().contains(trainee.getUserId())){
           throw  new IllegalArgumentException("trainee has already enrolled for this training");
       }

       List<Long> traineeIds = new ArrayList<>(training.getTraineeIds());
       traineeIds.add(trainee.getUserId());
       training.setTraineeIds(new ArrayList<>(traineeIds));

        traineeService.addTrainingToTrainee(trainee, training.getId());

        return trainingDAO.update(training);
    }

    @Override
    public Training withdrawFromTraining(Trainee trainee, Training training) {

        List<Long> traineeIds = new ArrayList<>(training.getTraineeIds());
        traineeIds.remove(trainee.getUserId());
        training.setTraineeIds(traineeIds);

        List<Long> trainingIds = new ArrayList<>(trainee.getTrainingsIds());
        trainingIds.remove(training.getId());
        trainee.setTrainingsIds(trainingIds);

        traineeService.update(trainee);

        return trainingDAO.update(training);
    }

    @Override
    public Training removeTrainingFromTrainer(Trainer trainer, Training training) {

            training.setTrainerId(null);

            List<Long> trainingIds = new ArrayList<>(trainer.getTrainingsIds());
            trainingIds.remove(training.getId());
            trainer.setTrainingsIds(trainingIds);

            trainerService.update(trainer);

        return trainingDAO.update(training);
    }

    @Override
    public void deleteById(Long id) {
        if(id == null){
            throw new NullPointerException("null id value was provided");
        }

        trainingDAO.delete(id);
    }


}
