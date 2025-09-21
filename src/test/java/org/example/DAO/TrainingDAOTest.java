package org.example.DAO;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.dao.impl.TrainingDAO;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.storage.Storage;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingDAOTest {

        @Mock
        private Storage storage;

        @InjectMocks
        private TrainingDAO trainingDAO;

        private Map<String, EntityInterface> map = new ConcurrentHashMap<>();
        private Training training1, training2;


        @BeforeEach
        void setUp(){

            training1 = Training.trainingBuilder()
                    .date(LocalDate.of(2025,9,17))
                    .type(TrainingType.CARDIO)
                    .name("Crossfit")
                    .duration(60L)
                    .trainerId(null)
                    .traineeIds(List.of())
                    .build();


            training2 = Training.trainingBuilder()
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

            when(storage.getById("training",1L)).thenReturn(training1);

            Training actual = trainingDAO.getById(1L);

            Assertions.assertNotNull(actual);
            Assertions.assertEquals("crossfit",actual.getName().toLowerCase());

            verify(storage).getById("training",1L);

        }

        @Test
        void getAllTest(){

            when(storage.getAllByNameSpace("training")).thenReturn(List.of(training1, training2));

            List<Training> actualList = trainingDAO.getAll();

            Assertions.assertEquals(2, actualList.size());
            Assertions.assertEquals(TrainingType.CARDIO, actualList.get(0).getTrainingType());

            verify(storage).getAllByNameSpace("training");

        }

        @Test
        void createTest(){
            Training newTraining = Training.trainingBuilder()
                    .date(LocalDate.of(2025,9,10))
                    .type(TrainingType.CALISTHENICS)
                    .name("Calisthenics")
                    .trainerId(null)
                    .duration(60L)
                    .traineeIds(List.of())
                    .build();

            when(storage.addToStorage(eq("training"),any(EntityInterface.class))).thenReturn(newTraining);

            Training actual = trainingDAO.create(newTraining);

            Assertions.assertNotNull(actual);
            Assertions.assertEquals(60L, actual.getDuration());

            verify(storage).addToStorage("training", newTraining);
        }

        @Test
        void updateTest(){
            Training updated = training1;
            updated.setId(1L);
            updated.setTrainerId(1L);
            updated.setDuration(75L);
            updated.setTraineeIds(List.of(1L,2L));

            when(storage.update("training:1", training1)).thenReturn(updated);

            Training actual = trainingDAO.update(training1);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(75L, actual.getDuration());
            Assertions.assertEquals(2, actual.getTraineeIds().size());
            Assertions.assertEquals(1L, actual.getTrainerId());

            verify(storage).update("training:1",training1);
        }

        @Test
        void deleteByIdTest(){
            trainingDAO.delete(1L);
            verify(storage).deleteFromStorage("training:1");
        }
}
