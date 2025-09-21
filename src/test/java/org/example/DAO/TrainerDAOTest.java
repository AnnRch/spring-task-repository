package org.example.DAO;

import org.example.dao.impl.TrainerDAO;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.storage.Storage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerDAOTest {

    @Mock
    private Storage storage;

    @InjectMocks
    private TrainerDAO trainerDAO;

    private Map<String, EntityInterface> map = new ConcurrentHashMap<>();
    private Trainer trainer1, trainer2;


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

        when(storage.getById("trainer",1L)).thenReturn(trainer1);

        Trainer actual = trainerDAO.getById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Lilia", actual.getFirstName());

        verify(storage).getById("trainer", 1L);

    }

    @Test
    void getAllTest(){

        when(storage.getAllByNameSpace("trainer")).thenReturn(List.of(trainer1, trainer2));

        List<Trainer> actualList = trainerDAO.getAll();

        Assertions.assertEquals(2, actualList.size());
        Assertions.assertEquals("Lilia", actualList.get(0).getFirstName());

        verify(storage).getAllByNameSpace("trainer");
    }

    @Test
    void createTest(){

            Trainer newTrainer = Trainer.trainerBuilder()
                    .specialization("fitness trainer")
                    .isActive(false)
                    .trainingsIds(List.of())
                    .trainingTypes(List.of(TrainingType.CARDIO,TrainingType.CALISTHENICS))
                    .firstName("Evgenia")
                    .lastName("Svitla")
                    .build();

            when(storage.addToStorage(eq("trainer"), any(EntityInterface.class))).thenReturn(newTrainer);

            Trainer actual = trainerDAO.create(newTrainer);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals("Evgenia", actual.getFirstName());

            verify(storage).addToStorage("trainer", newTrainer);
    }

    @Test
    void updateTest(){
        Trainer updated = trainer1;
        updated.setTrainingsIds(List.of(1L,2L));
        updated.setActive(true);
        updated.setUserId(3L);

        when(storage.update("trainer:3",trainer1)).thenReturn(updated);

        Trainer actual = trainerDAO.update(trainer1);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isActive());
        Assertions.assertEquals(2, actual.getTrainingsIds().size());

        verify(storage).update("trainer:3",trainer1);
    }

    @Test
    void deleteByIdTest(){

        trainerDAO.delete(1L);
        verify(storage).deleteFromStorage("trainer:1");
    }
}
