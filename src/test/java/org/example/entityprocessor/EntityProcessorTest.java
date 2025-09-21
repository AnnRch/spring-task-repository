package org.example.entityprocessor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.credentials.PasswordGenerator;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.processing.EntityProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntityProcessorTest {

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private EntityProcessor entityProcessor;

    private Map<String, EntityInterface> map = new ConcurrentHashMap<>();
    private Trainee trainee;
    private Trainer trainer;
    private Training training;

    @BeforeEach
    void setUp(){

        trainee = Trainee.traineeBuilder()
                .address("Melitopol")
                .dateOfBirth(LocalDate.of(1986,6,27))
                .isActive(false)
                .lastName("Sobol")
                .firstName("Anna")
                .trainingsIds(List.of())
                .build();

        trainer = Trainer.trainerBuilder()
                .specialization("fitness trainer")
                .firstName("Lilia")
                .lastName("Levada")
                .trainingsIds(List.of())
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.PILATES))
                .isActive(false)
                .build();


        training = Training.trainingBuilder()
                .date(LocalDate.of(2025,9,17))
                .type(TrainingType.CARDIO)
                .name("Crossfit")
                .duration(60L)
                .trainerId(null)
                .traineeIds(List.of())
                .build();


    }

    @Test
    void createTraineeTest(){

        when(passwordGenerator.generatePassword()).thenReturn("ety&*0plkj");

        EntityInterface actual = entityProcessor.create(map, "trainee:1", trainee);

        Assertions.assertInstanceOf(Trainee.class, actual);
        verify(passwordGenerator).generatePassword();

    }

    @Test
    void createTrainerTest(){

        when(passwordGenerator.generatePassword()).thenReturn("45fg@#jki8");
        EntityInterface actual = entityProcessor.create(map, "trainer:1", trainer);

        Assertions.assertInstanceOf(Trainer.class, actual);
        verify(passwordGenerator).generatePassword();

    }

    @Test
    void createTrainingTest(){

        EntityInterface actual = entityProcessor.create(map, "training:1", training);
        Assertions.assertInstanceOf(Training.class, actual);
    }

    @Test
    void updateTraineeTest(){

        map.put("trainee:1",trainee);

        Trainee updated = trainee;
        updated.setTrainingsIds(List.of(1L,2L));
        updated.setActive(true);

        EntityInterface actual = entityProcessor.update(map, "trainee:1",trainee);
        Assertions.assertSame(updated, actual);
    }

    @Test
    void updateTrainerTest(){

        map.put("trainer:1", trainer);

        Trainer updated = trainer;
        updated.setUserId(10L);
        updated.setSpecialization("pilates instructor");

        EntityInterface actual = entityProcessor.update(map, "trainer:1", trainer);
        Assertions.assertSame(updated, actual);
    }

    @Test
    void updateTrainingTest(){
        map.put("training:1", training);

        Training updated = training;
        updated.setDuration(80L);
        updated.setTraineeIds(List.of(1L,10L));

        EntityInterface actual = entityProcessor.update(map, "training:1", training);
        Assertions.assertSame(updated, actual);
    }


}
