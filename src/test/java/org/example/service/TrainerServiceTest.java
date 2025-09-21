package org.example.service;

import org.example.dao.impl.TrainerDAO;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.service.impl.TrainerServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer1, trainer2;

    private Map<String, EntityInterface> map = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp(){
        trainer1 = Trainer.trainerBuilder()
                .specialization("fitness trainer")
                .firstName("Lilia")
                .lastName("Levada")
                .trainingsIds(List.of())
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.PILATES))
                .isActive(false)
                .build();

        trainer2 = Trainer.trainerBuilder()
                .specialization("fitness trainer")
                .firstName("Marina")
                .lastName("Verhovcka")
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.CALISTHENICS, TrainingType.STRETCHING))
                .trainingsIds(List.of())
                .isActive(true)
                .build();

        map.put("trainer:1", trainer1);
        map.put("trainer:2",trainer2);
    }

    @Test
    void getByIdTest(){
        when(trainerDAO.getById(1L)).thenReturn(trainer1);

        Trainer actual = trainerService.getById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Lilia", actual.getFirstName());

        verify(trainerDAO).getById(1L);
    }

    @Test
    void getAllTest(){

        when(trainerDAO.getAll()).thenReturn(List.of(trainer1, trainer2));

       List<Trainer> actualList = trainerService.getAll();

       Assertions.assertEquals(2, actualList.size());
       Assertions.assertEquals("Lilia", actualList.get(0).getFirstName());

       verify(trainerDAO).getAll();

    }

    @Test
    void createTest(){
        Trainer newTrainer = Trainer.trainerBuilder()
                .specialization("fitness trainer")
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.CALISTHENICS))
                .firstName("Marina")
                .lastName("Solyarchina")
                .isActive(false)
                .trainingsIds(List.of())
                .build();

        when(trainerDAO.create(any(Trainer.class))).thenReturn(newTrainer);

        Trainer actual = trainerService.create(newTrainer);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Marina", actual.getFirstName());

        verify(trainerDAO).create(newTrainer);
    }

    @Test
    void updateTest(){
        Trainer updated = trainer1;
        updated.setTrainingsIds(List.of(1L, 2L));
        updated.setActive(true);

        when(trainerDAO.update(any(Trainer.class))).thenReturn(updated);

        Trainer actual = trainerService.update(updated);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(List.of(1L, 2L), actual.getTrainingsIds());
        Assertions.assertTrue(actual.isActive());

        verify(trainerDAO).update(updated);
    }

    @Test
    void deleteTest(){
        trainerService.deleteById(1L);
        verify(trainerDAO).delete(1L);
    }

    @Test
    void addTrainingToTrainerTest(){

    Training training2 = Training.trainingBuilder()
                .id(10L)
                .traineeIds(List.of())
                .duration(50L)
                .trainerId(null)
                .name("Pilates with weights")
                .type(TrainingType.PILATES)
                .date(LocalDate.of(2025, 9, 18))
                .build();

        trainer1.setUserId(1L);
        trainerService.addTrainingToTrainer(trainer1, training2.getId());

        Assertions.assertEquals(List.of(10L), trainer1.getTrainingsIds());
    }
}
