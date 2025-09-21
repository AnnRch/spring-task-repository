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
            log.error("enrolling failed due to null values");
                throw new NullPointerException("null values");
        }

        log.info("Attempting to enroll trainee {} to training {}", trainee.getUserId(), training.getId());

        Trainee currentTrainee = traineeService.getById(trainee.getUserId());
        if(currentTrainee == null){

            log.info("Trainee {} not found; creating new trainee", trainee.getUserId());
            currentTrainee = traineeService.create(trainee);
        }

        return trainingService.enrollForTraining(currentTrainee, training);
    }

    public Training createTraining(Trainer trainer, Training training){

            if(trainer == null || training == null){
                log.error("null trainer or trainee");
                throw new NullPointerException("null values");
            }

            log.info("Attempting to create training for trainer {}",trainer.getUserId() );
            Trainer currenttrainer = trainerService.getById(trainer.getUserId());

            if(currenttrainer == null){
                log.info("Trainer {} not found; creating new trainer", trainer.getUserId());
                currenttrainer = trainerService.create(trainer);
            }

            return trainingService.createTrainingByTrainer(currenttrainer, training);
    }

  public Training withdrawFromTraining(Trainee trainee, Training training){

      if(trainee == null || training == null){
          log.error("withdraw process failed due to null values");
          throw new NullPointerException("null values");
      }

      Trainee currentTrainee = traineeService.getById(trainee.getUserId());

      if(currentTrainee ==  null){
          log.error("trainee with id {} was not found", trainee.getUserId());
          throw  new NullPointerException("no such trainee in storage");
      }

      if(!training.getTraineeIds().contains(trainee.getUserId())){
          log.warn("Trainee {} can't be withdrawn due to not being enrolled for training {}",
                    trainee.getUserId(), training.getId()
                  );
          throw new NullPointerException("trainee hasn't been enrolled for this training");
      }

       return trainingService.withdrawFromTraining(trainee, training);
  }

  public Training removeTrainingFromTrainer(Trainer trainer, Training training){

        if(trainer == null || training == null){
            log.error("Removing failed due to null values");
            throw  new NullPointerException("null values");
        }


        Trainer currentTrainer = trainerService.getById(trainer.getUserId());
        if(currentTrainer == null){
            log.error("Trainer with id {} is not present in storage", trainer.getUserId());
            throw new NullPointerException("trainer isn't present in storage");
        }

        if(!training.getTrainerId().equals(trainer.getUserId())){
            log.warn("Training {} wasn't removed from trainer {} ", training.getId(), trainer.getUserId() );
            throw new NullPointerException("trainer hasn't created this training");
        }

        return trainingService.removeTrainingFromTrainer(trainer, training);
  }


   public List<Trainee> getAllEnrolledTrainees(Training training){
        if(training == null){
            log.error("getting all trainees failed due to null values ");
            throw  new NullPointerException("null value for training");
        }

        log.info("getting the list of all trainees enrolled on training {}", training.getId());
        return training.getTraineeIds().stream()
                .map(traineeService::getById)
                .toList();
   }

    public List<Training> getAllTrainingsByTrainer(Trainer trainer){

        //use logging
        if(trainer == null || trainer.getUserId() == null){
            log.error("getting the list of trainings by traine failed due to null values ");
            throw  new NullPointerException("null values were provided");
        }

        log.info("getting list of trainings for trainer {}", trainer.getUserId());
        return trainerService.getById(trainer.getUserId()).getTrainingsIds().stream()
                .map(trainingService::getById)
                .toList();
    }

    public List<Training> getAllTrainingsByDate(LocalDate date){
        if(date == null){
            log.error("operation failed due to null value for local date");
            throw new NullPointerException("null date was given");
        }

        return trainingService.getAllByDate(date);
    }

}
