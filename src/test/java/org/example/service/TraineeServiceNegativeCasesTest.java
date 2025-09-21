package org.example.service;

import org.example.dao.impl.TraineeDAO;
import org.example.model.impl.Trainee;
import org.example.model.impl.Training;
import org.example.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AssertionsKt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceNegativeCasesTest {

    @Mock
    private TraineeDAO traineeDAO;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee, trainee1;
    private Training training;

    @Test
    void getByIdNullCase(){

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> traineeService.getById(null));

        Assertions.assertEquals("null id was provided", exception.getMessage());
    }

    @Test
    void createNullCase(){

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> traineeService.create(null)
        );

        Assertions.assertEquals("null value was provided", exception.getMessage());
    }

    @Test
    void updateNullCase(){

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> traineeService.update(null)
        );

        Assertions.assertEquals("null value was provided", exception.getMessage());

    }

    @Test
    void addTrainingToTraineeWrongCase(){

        Training training1 = Training.trainingBuilder()
                .id(1L)
                .build();

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> traineeService.addTrainingToTrainee(null, training1.getId())
        );

        Assertions.assertEquals("null values", exception.getMessage());
    }

}
