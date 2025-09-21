package org.example.processing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.credentials.PasswordGenerator;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityProcessor {

    private final PasswordGenerator passwordGenerator;

    private boolean isPresentInStorage(String firstName, String lastName, Map<String, EntityInterface> entityMap){
        for(Map.Entry<String, EntityInterface> entry : entityMap.entrySet()){
            if(entry.getValue() instanceof Trainee trainee){
                return trainee.getFirstName().equals(firstName) &&
                        trainee.getLastName().equals(lastName);
            }

            else if(entry.getValue() instanceof Trainer trainer){
                return trainer.getFirstName().equals(firstName) &&
                        trainer.getLastName().equals(lastName);
            }
        }
        return false;
    }

    private  String generateUserName(Map<String, EntityInterface> entityMap,String firstName, String lastName, Long id){
        return isPresentInStorage(firstName, lastName, entityMap)
                ? firstName + "." + lastName + id
                : firstName + "." + lastName;
    }

    public EntityInterface create(Map<String, EntityInterface> storage, String key, EntityInterface value){

        log.debug("creating entity with type : " + key);
        Long id = key.contains(":")
                ? Long.parseLong(key.substring(key.indexOf(":") + 1))
                : null;

        if(value instanceof Trainer trainer){
            return Trainer.trainerBuilder()
                    .firstName(trainer.getFirstName())
                    .lastName(trainer.getLastName())
                    .trainingTypes(trainer.getTrainingTypes())
                    .specialization(trainer.getSpecialization())
                    .isActive(false)
                    .userId(id)
                    .trainingsIds(trainer.getTrainingsIds())
                    .password(passwordGenerator.generatePassword())
                    .userName(generateUserName(storage, trainer.getFirstName(), trainer.getLastName(), trainer.getUserId()))
                    .build();
        }

        else if(value instanceof Trainee  trainee){
            return Trainee.traineeBuilder()
                    .firstName(trainee.getFirstName())
                    .lastName(trainee.getLastName())
                    .address(trainee.getAddress())
                    .dateOfBirth(trainee.getDateOfBirth() != null ? trainee.getDateOfBirth() : LocalDate.now())
                    .userId(id)
                    .password(passwordGenerator.generatePassword())
                    .userName(generateUserName(storage, trainee.getFirstName(), trainee.getLastName(), trainee.getUserId()))
                    .trainingsIds(trainee.getTrainingsIds())
                    .build();
        }

        else if(value instanceof Training training){
            System.out.println(training.getTrainingType().toString());
            return Training.trainingBuilder()
                    .id(id)
                    .name(training.getName())
                    .type(training.getTrainingType() != null
                            ? training.getTrainingType() : TrainingType.CARDIO)
                    .date(training.getDate() != null ? training.getDate() : LocalDate.now())
                    .duration(training.getDuration())
                    .trainerId(training.getTrainerId())
                    .traineeIds(training.getTraineeIds())
                    .build();
        }

        return null;
    }


    public EntityInterface update(Map<String,EntityInterface> storage, String key, EntityInterface value){

        if(value instanceof Trainer trainer){
            Trainer existingTrainer = (Trainer) storage.get(key);
            existingTrainer.setLastName(trainer.getLastName());
            existingTrainer.setFirstName(trainer.getFirstName());
            existingTrainer.setTrainingsIds(trainer.getTrainingsIds());
            existingTrainer.setActive(trainer.isActive());
            existingTrainer.setSpecialization(trainer.getSpecialization());
            existingTrainer.setTrainingTypes(trainer.getTrainingTypes());

            return storage.put(key, existingTrainer);
        }

        else if (value instanceof Trainee trainee){
            Trainee existingTrainee = (Trainee) storage.get(key);
            existingTrainee.setAddress(trainee.getAddress());
            existingTrainee.setActive(trainee.isActive());
            existingTrainee.setDateOfBirth(trainee.getDateOfBirth());
            existingTrainee.setTrainingsIds(trainee.getTrainingsIds());
            existingTrainee.setFirstName(trainee.getFirstName());
            existingTrainee.setLastName(trainee.getLastName());

            return storage.put(key, existingTrainee);
        }

        else if(value instanceof  Training training){
            Training existingTraining = (Training) storage.get(key);
            existingTraining.setName(training.getName());
            existingTraining.setDate(training.getDate());
            existingTraining.setTrainingType(training.getTrainingType());
            existingTraining.setDuration(training.getDuration());
            existingTraining.setTrainerId(training.getTrainerId());
            existingTraining.setTraineeIds(training.getTraineeIds());

            return storage.put(key, existingTraining);
        }

        return null;
    }
}
