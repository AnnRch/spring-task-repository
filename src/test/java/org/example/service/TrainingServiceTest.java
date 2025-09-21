package org.example.service;

import org.example.dao.impl.TrainingDAO;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.service.impl.TraineeServiceImpl;
import org.example.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeServiceImpl traineeService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training1, training2;

    private Map<String, EntityInterface> map  = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp(){
        training1 = Training.trainingBuilder()
                .id(1L)
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of())
                .build();


        training2 = Training.trainingBuilder()
                .id(2L)
                .traineeIds(List.of())
                .duration(50L)
                .trainerId(null)
                .name("Pilates with weights")
                .type(TrainingType.PILATES)
                .date(LocalDate.of(2025, 9, 18))
                .build();

        map.put("training:1",training1);
        map.put("training:2", training2);
    }

    @Test
    void getByIdTest(){

        when(trainingDAO.getById(1L)).thenReturn(training1);

        Training actual = trainingService.getById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("crossfit", actual.getName().toLowerCase());

        verify(trainingDAO).getById(1L);
    }

    @Test
    void getAllTest(){

        when(trainingDAO.getAll()).thenReturn(List.of(training1, training2));

       List<Training> actualList = trainingService.getAll();
       Assertions.assertEquals(2, actualList.size());
       Assertions.assertEquals("crossfit", actualList.get(0).getName().toLowerCase());

       verify(trainingDAO).getAll();

    }

    @Test
    void getAllByDate(){
        when(trainingDAO.getAll()).thenReturn(List.of(training1, training2));

        List<Training> actualList = trainingService.getAllByDate(LocalDate.of(2025,9,18));
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals(2L, actualList.get(0).getId());

        verify(trainingDAO).getAll();
    }

    @Test
    void createTest(){
        Training newTraining = Training.trainingBuilder()
                .date(LocalDate.of(2025, 9, 19))
                .name("power hiit")
                .id(3L)
                .type(TrainingType.CARDIO)
                .traineeIds(List.of())
                .duration(60L)
                .trainerId(null)
                .build();

        when(trainingDAO.create(any(Training.class))).thenReturn(newTraining);

        Training actual = trainingService.create(newTraining);
        Assertions.assertEquals(3L, actual.getId());
        Assertions.assertEquals(TrainingType.CARDIO, actual.getTrainingType());

        verify(trainingDAO).create(newTraining);
    }

    @Test
    void updateTest(){
        Training updated = training1;
        updated.setDuration(50L);
        updated.setTrainerId(10L);

        when(trainingDAO.update(any(Training.class))).thenReturn(updated);
        Training actual = trainingService.update(updated);

        Assertions.assertEquals(50L, actual.getDuration());
        Assertions.assertEquals(10L, actual.getTrainerId());

        verify(trainingDAO).update(updated);

    }

    @Test
    void deleteByIdTest(){
        trainingService.deleteById(1L);
        verify(trainingDAO).delete(1L);
    }

    @Test
    void createTrainingByTrainerTest(){
        Trainer trainer1 = Trainer.trainerBuilder()
                .userId(10L)
                .specialization("fitness trainer")
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.CALISTHENICS))
                .firstName("Marina")
                .lastName("Solyarchina")
                .isActive(false)
                .trainingsIds(List.of())
                .build();

        when(trainingDAO.update(training1)).thenReturn(training1);

        Training actual = trainingService.createTrainingByTrainer(trainer1, training1);

        Assertions.assertEquals(trainer1.getUserId(), actual.getTrainerId());
        verify(trainerService).addTrainingToTrainer(trainer1, training1.getId());

    }

    @Test
    void enrollForTrainingTest(){
        Trainee trainee = Trainee.traineeBuilder()
                .userId(3L)
                .dateOfBirth(LocalDate.of(1984,7,14))
                .address("Melitopol")
                .firstName("Alesya")
                .lastName("Kalinina")
                .trainingsIds(List.of())
                .isActive(true)
                .build();


        when(trainingDAO.update(training1)).thenReturn(training1);

        Training actual = trainingService.enrollForTraining(trainee, training1);
        Assertions.assertTrue(actual.getTraineeIds().contains(trainee.getUserId()));

        verify(traineeService).addTrainingToTrainee(trainee, training1.getId());
    }

    @Test
    void withdrawFromTrainingTest(){
        Trainee trainee = Trainee.traineeBuilder()
                .userId(3L)
                .dateOfBirth(LocalDate.of(1984,7,14))
                .address("Melitopol")
                .firstName("Alesya")
                .lastName("Kalinina")
                .trainingsIds(List.of())
                .isActive(true)
                .build();


        when(trainingDAO.update(training1)).thenReturn(training1);

        Training actual = trainingService.withdrawFromTraining(trainee, training1);

        Assertions.assertFalse(actual.getTraineeIds().contains(trainee.getUserId()));
        Assertions.assertFalse(trainee.getTrainingsIds().contains(training1.getId()));

        verify(traineeService).update(trainee);
        verify(trainingDAO).update(training1);

    }

    @Test
    void removeTrainingFromTrainerTest(){
        Trainer trainer = Trainer.trainerBuilder()
                .userId(10L)
                .specialization("fitness trainer")
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.CALISTHENICS))
                .firstName("Marina")
                .lastName("Solyarchina")
                .isActive(false)
                .trainingsIds(List.of())
                .build();

        when(trainingDAO.update(training1)).thenReturn(training1);

        Training actual = trainingService.removeTrainingFromTrainer(trainer, training1);
        Assertions.assertNull(actual.getTrainerId());
        Assertions.assertFalse(trainer.getTrainingsIds().contains(training1.getId()));

        verify(trainerService).update(trainer);
        verify(trainingDAO).update(training1);
    }
}
