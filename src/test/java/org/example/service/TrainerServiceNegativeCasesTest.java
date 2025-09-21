package org.example.service;

import org.example.dao.impl.TrainerDAO;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceNegativeCasesTest {

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;
    private Training training;

    @Test
    void getByIdNullCase(){

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> trainerService.getById(null));

        Assertions.assertEquals("null id was given", exception.getMessage());

    }

    @Test
    void createNullCase(){

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> trainerService.create(null)
        );

        Assertions.assertEquals("null trainer value", exception.getMessage());
    }

    @Test
    void updateNullCase(){

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> trainerService.update(null)
        );

        Assertions.assertEquals("null trainer value", exception.getMessage());

    }


    @Test
    void addTrainingToTrainerWrongCase(){
        Training training1 = Training.trainingBuilder()
                .id(1L)
                .build();

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> trainerService.addTrainingToTrainer(null, training1.getId())
        );

        Assertions.assertEquals("null values", exception.getMessage());
    }


}
