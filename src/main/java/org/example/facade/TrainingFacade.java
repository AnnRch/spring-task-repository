package org.example.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingFacade {

    private final TrainingService trainingService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public Training enrollForTraining(Trainee trainee, Training training){
        if(training == null || trainee == null) {
                throw new NullPointerException("null values");
        }

        Trainee currentTrainee = traineeService.getById(trainee.getUserId());
        if(currentTrainee == null){

            currentTrainee = traineeService.create(trainee);
        }

        return trainingService.enrollForTraining(currentTrainee, training);
    }

    public Training createTraining(Trainer trainer, Training training){

            if(trainer == null || training == null){
                throw new NullPointerException("null values");
            }

            Trainer currenttrainer = trainerService.getById(trainer.getUserId());

            if(currenttrainer == null){
                currenttrainer = trainerService.create(trainer);
            }

            return trainingService.createTrainingByTrainer(currenttrainer, training);
    }

  public Training withdrawFromTraining(Trainee trainee, Training training){

      if(trainee == null || training == null){
          throw new NullPointerException("null values");
      }

      Trainee currentTrainee = traineeService.getById(trainee.getUserId());

      if(currentTrainee ==  null){
          throw  new NullPointerException("no such trainee in storage");
      }

      if(!training.getTraineeIds().contains(trainee.getUserId())){
          throw new NullPointerException("trainee hasn't been enrolled for this training");
      }

       return trainingService.withdrawFromTraining(trainee, training);
  }

  public Training removeTrainingFromTrainer(Trainer trainer, Training training){

        if(trainer == null || training == null){
            throw  new NullPointerException("null values");
        }


        Trainer currentTrainer = trainerService.getById(trainer.getUserId());
        if(currentTrainer == null){
            throw new NullPointerException("trainer isn't present in storage");
        }

        if(!training.getTrainerId().equals(trainer.getUserId())){
            throw new NullPointerException("trainer hasn't created this training");
        }

        return trainingService.removeTrainingFromTrainer(trainer, training);
  }


   public List<Trainee> getAllEnrolledTrainees(Training training){
        if(training == null){
            throw  new NullPointerException("null value for training");
        }

        return training.getTraineeIds().stream()
                .map(traineeService::getById)
                .toList();
   }

    public List<Training> getAllTrainingsByTrainer(Trainer trainer){

        //use logging
        if(trainer == null || trainer.getUserId() == null){
            throw  new NullPointerException("null values were provided");
        }

        return trainerService.getById(trainer.getUserId()).getTrainingsIds().stream()
                .map(trainingService::getById)
                .toList();
    }

    public List<Training> getAllTrainingsByDate(LocalDate date){
        if(date == null){
            throw new NullPointerException("null date was given");
        }

        return trainingService.getAllByDate(date);
    }

}
