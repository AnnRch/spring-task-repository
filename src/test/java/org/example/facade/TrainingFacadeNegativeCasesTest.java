package org.example.facade;

import io.micrometer.observation.Observation;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingFacadeNegativeCasesTest {

    @Mock
    private TrainingService trainingService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingFacade trainingFacade;

    private Trainer trainer;
    private Trainee trainee;
    private Training training;


    @BeforeEach
    void setUp(){
        trainer = Trainer.trainerBuilder()
                .userId(10L)
                .build();

        trainee = Trainee.traineeBuilder()
                .userId(1L)
                .build();

        training = Training.trainingBuilder()
                .id(1L)
                .traineeIds(List.of())
                .build();
    }

    @Test
    void enrollForTrainingNullTrainee(){

        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            trainingFacade.enrollForTraining(null, training);
        });

        Assertions.assertEquals("null values", exception.getMessage());
    }


    @Test
    void enrollForTrainingNullTraining(){
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            trainingFacade.enrollForTraining(trainee, null);
        });

        Assertions.assertEquals("null values", exception.getMessage());
    }



    @Test
    void enrollForTrainingWrongTraineeId(){

       Training training1 = Training.trainingBuilder()
                .id(1L)
               .traineeIds(List.of(1L))
                .build();


        when(traineeService.getById(anyLong())).thenReturn(null);
        when(traineeService.create(trainee)).thenReturn(trainee);
        when(trainingService.enrollForTraining(trainee, training)).thenReturn(training1);

        Training actual = trainingFacade.enrollForTraining(trainee, training);
        Assertions.assertEquals(training1.getTraineeIds().size(), actual.getTraineeIds().size());
        Assertions.assertTrue(actual.getTraineeIds().contains(trainee.getUserId()));

        verify(traineeService).getById(anyLong());
        verify(traineeService).create(trainee);
        verify(trainingService).enrollForTraining(trainee, training);
    }

    @Test
    void createTrainingWithNullTrainer(){

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> trainingFacade.createTraining(trainer, null));

        Assertions.assertEquals("null values", exception.getMessage());
    }

    @Test
    void withdrawFromTrainingForWrongTrainee(){

        Training training1 = Training.trainingBuilder()
                .id(1L)
                .traineeIds(List.of(2L, 3L))
                .build();

        when(traineeService.getById(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(
                NullPointerException.class,
                () -> trainingFacade.withdrawFromTraining(trainee, training1));

    }

    @Test
    void removeTrainingFromTrainerWrongTrainer(){

        Training training1 = Training.trainingBuilder()
                .id(1L)
                .trainerId(200L)
                .build();

        when(trainerService.getById(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(
                NullPointerException.class,
                () -> trainingFacade.removeTrainingFromTrainer(trainer, training1)
        );
    }

}
