package org.example.facade;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.source.tree.AssertTree;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingFacade trainingFacade;

    private Trainee trainee;
    private Trainer trainer;
    private Training training;

    @BeforeEach
    void setUp(){
        trainee = Trainee.traineeBuilder()
                .userId(1L)
                .address("Melitopol")
                .dateOfBirth(LocalDate.of(1986,6,27))
                .isActive(false)
                .lastName("Sobol")
                .firstName("Anna")
                .trainingsIds(List.of())
                .build();

        trainer = Trainer.trainerBuilder()
                .userId(1L)
                .specialization("fitness trainer")
                .firstName("Lilia")
                .lastName("Levada")
                .trainingsIds(List.of())
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.PILATES))
                .isActive(false)
                .build();


        training = Training.trainingBuilder()
                .id(10L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of())
                .build();
    }

    @Test
    void enrollForTrainingTest(){

        Training updated = training;
        updated.setTraineeIds(List.of(1L));

        when(traineeService.getById(1L)).thenReturn(trainee);
        when(trainingService.enrollForTraining(trainee,training)).thenReturn(updated);

        Training actual = trainingFacade.enrollForTraining(trainee, training);
        Assertions.assertSame(updated, actual);
        Assertions.assertTrue(actual.getTraineeIds().contains(trainee.getUserId()));

        verify(traineeService).getById(1L);
        verify(trainingService).enrollForTraining(trainee, training);

    }

    @Test
    void createTrainingTest(){
        Training updated = training;
        updated.setTrainerId(1L);

        when(trainerService.getById(1L)).thenReturn(trainer);
        when(trainingService.createTrainingByTrainer(trainer, training)).thenReturn(updated);

        Training actual = trainingFacade.createTraining(trainer, training);
        Assertions.assertSame(updated, actual);

        verify(trainerService).getById(1L);
        verify(trainingService).createTrainingByTrainer(trainer,training);
    }

    @Test
    void withdrawFromTrainingTest(){

        Training updated = Training.trainingBuilder()
                .id(10L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of(1L))
                .build();


        Training expected = Training.trainingBuilder()
                .id(10L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of())
                .build();

        when(traineeService.getById(1L)).thenReturn(trainee);
        when(trainingService.withdrawFromTraining(trainee, updated)).thenReturn(expected);

        Training actual = trainingFacade.withdrawFromTraining(trainee, updated);
        Assertions.assertFalse(actual.getTraineeIds().contains(trainee.getUserId()));
        Assertions.assertSame(expected, actual);

        verify(traineeService).getById(1L);
        verify(trainingService).withdrawFromTraining(trainee, updated);

    }

    @Test
    void removeTrainingFromTrainerTest(){

        Training updated = Training.trainingBuilder()
                .id(10L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(1L)
                .traineeIds(List.of())
                .build();


        Training expected = Training.trainingBuilder()
                .id(10L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of())
                .build();


        when(trainerService.getById(1L)).thenReturn(trainer);
        when(trainingService.removeTrainingFromTrainer(trainer,updated)).thenReturn(expected);

        Training actual = trainingFacade.removeTrainingFromTrainer(trainer, updated);
        Assertions.assertSame(expected, actual);

        verify(trainerService).getById(1L);
        verify(trainingService).removeTrainingFromTrainer(trainer,updated);
    }

    @Test
    void getAllEnrolledTraineesTest(){
        Trainee trainee1 = Trainee.traineeBuilder()
                .userId(2L)
                .address("Melitopol")
                .dateOfBirth(LocalDate.of(1986,6,27))
                .isActive(false)
                .lastName("Ageeva")
                .firstName("Natalia")
                .trainingsIds(List.of())
                .build();


        Training currentTraining = Training.trainingBuilder()
                .id(10L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of(1L,2L))
                .build();

        List<Trainee> expected = List.of(trainee, trainee1);

        when(traineeService.getById(1L)).thenReturn(trainee);
        when(traineeService.getById(2L)).thenReturn(trainee1);

        List<Trainee> actual = trainingFacade.getAllEnrolledTrainees(currentTraining);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());

        verify(traineeService, times(2)).getById(anyLong());

    }

    @Test
    void getAllTrainingsByTrainerTest(){
        Trainer current = Trainer.trainerBuilder()
                .userId(1L)
                .specialization("fitness trainer")
                .firstName("Lilia")
                .lastName("Levada")
                .trainingsIds(List.of(10L,20L))
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.PILATES))
                .isActive(false)
                .build();


        Training currentTraining = Training.trainingBuilder()
                .id(20L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of(1L,2L))
                .build();


        List<Training> expected = List.of(training, currentTraining);

        when(trainerService.getById(1L)).thenReturn(current);
        when(trainingService.getById(10L)).thenReturn(training);
        when(trainingService.getById(20L)).thenReturn(currentTraining);

        List<Training> actual = trainingFacade.getAllTrainingsByTrainer(current);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());

        verify(trainingService, times(2)).getById(anyLong());
    }

    @Test
    void getAllTrainingsByDateTest(){

        Training training1 = Training.trainingBuilder()
                .id(20L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of())
                .build();

        Training training2 = Training.trainingBuilder()
                .id(20L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.STRETCHING)
                .name("Strong yoga")
                .duration(45L)
                .trainerId(null)
                .traineeIds(List.of())
                .build();

        List<Training> expected = List.of(training1, training2);

        when(trainingService.getAllByDate(LocalDate.of(2025, 9,17)))
                .thenReturn(expected);

        List<Training> actual = trainingFacade.getAllTrainingsByDate(LocalDate.of(2025, 9,17));
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());

        verify(trainingService).getAllByDate(LocalDate.of(2025, 9, 17));

    }

}
