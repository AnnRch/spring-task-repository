package org.example.service;

import org.example.dao.impl.TrainingDAO;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceNegativeCasesTest {

        @Mock
        private TrainingDAO trainingDAO;

        @Mock
        private TraineeService traineeService;

        @Mock
        private TrainerService trainerService;

        @InjectMocks
        private TrainingServiceImpl trainingService;

        private Trainee trainee;
        private Trainer trainer;
        private Training training;


        @BeforeEach
        void setUp(){

            training = Training.trainingBuilder()
                    .id(1L)
                    .date(LocalDate.of(2025,9,17))
                    .type(TrainingType.CARDIO)
                    .name("Crossfit")
                    .duration(60L)
                    .trainerId(10L)
                    .traineeIds(List.of())
                    .build();

            trainee = Trainee.traineeBuilder()
                    .userId(20L)
                    .dateOfBirth(LocalDate.of(1984,7,14))
                    .address("Melitopol")
                    .firstName("Alesya")
                    .lastName("Kalinina")
                    .trainingsIds(List.of())
                    .isActive(true)
                    .build();


            trainer = Trainer.trainerBuilder()
                    .userId(10L)
                    .specialization("fitness trainer")
                    .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.CALISTHENICS))
                    .firstName("Marina")
                    .lastName("Solyarchina")
                    .isActive(false)
                    .trainingsIds(List.of(1L))
                    .build();
        }

        @Test
        void createTrainingByTrainerAlreadyExistsCase(){

            IllegalArgumentException exception = Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> trainingService.createTrainingByTrainer(trainer, training));

            Assertions.assertEquals("trainer has already assigned for this training",exception.getMessage());
        }

        @Test
        void enrollForTrainingAlreadyEnrolledCase(){

        Trainee trainee1 = Trainee.traineeBuilder()
                    .userId(20L)
                    .dateOfBirth(LocalDate.of(1984,7,14))
                    .address("Melitopol")
                    .firstName("Alesya")
                    .lastName("Kalinina")
                    .trainingsIds(List.of(1L))
                    .isActive(true)
                    .build();

            Training training1 = Training.trainingBuilder()
                    .id(1L)
                    .date(LocalDate.of(2025,9,17))
                    .type(TrainingType.CARDIO)
                    .name("Crossfit")
                    .duration(60L)
                    .trainerId(10L)
                    .traineeIds(List.of(20L))
                    .build();


            IllegalArgumentException exception = Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> trainingService.enrollForTraining(trainee1, training1));

            Assertions.assertEquals("trainee has already enrolled for this training", exception.getMessage());
        }

   @Test
   void enrollForTrainingNullCase(){

           NullPointerException exception = Assertions.assertThrows(
                   NullPointerException.class,
                   () -> trainingService.enrollForTraining(null, training)
           );

           Assertions.assertEquals("null values were given",exception.getMessage());
   }
}
