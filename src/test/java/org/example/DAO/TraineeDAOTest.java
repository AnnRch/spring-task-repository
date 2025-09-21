package org.example.DAO;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.dao.impl.TraineeDAO;
import org.example.model.EntityInterface;
import org.example.model.impl.Trainee;
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
public class TraineeDAOTest {

    @Mock
    private Storage storage;

    private Map<String, EntityInterface> mockedMap;
    private Trainee trainee1, trainee2;

    @InjectMocks
    private TraineeDAO traineeDAO;

    @BeforeEach
    void setStorage(){
        mockedMap = new ConcurrentHashMap<>();

        trainee1 = Trainee.traineeBuilder()
                .address("Melitopol")
                .dateOfBirth(LocalDate.of(1986,6,27))
                .isActive(false)
                .lastName("Sobol")
                .firstName("Anna")
                .trainingsIds(List.of())
                .build();

        trainee2 = Trainee.traineeBuilder()
                .address("Dnipro")
                .trainingsIds(List.of())
                .dateOfBirth(LocalDate.of(1983,6,18))
                .lastName("Bondar")
                .firstName("Natalia")
                .isActive(false)
                .build();

        mockedMap.put("trainee:1",trainee1);
        mockedMap.put("trainee:2",trainee2);
    }

    @Test
    void getByIdTest(){
        when(storage.getById("trainee",1L)).thenReturn(trainee1);

        Trainee actual = traineeDAO.getById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Anna",actual.getFirstName());
        Assertions.assertEquals("Melitopol",actual.getAddress());

        verify(storage).getById("trainee",1L);
    }

    @Test
    void getAllTest(){

        when(storage.getAllByNameSpace("trainee")).thenReturn(List.of(trainee1, trainee2));

        List<Trainee> actialList = traineeDAO.getAll();
        Assertions.assertNotNull(actialList);
        Assertions.assertEquals(2, actialList.size());
        Assertions.assertEquals(trainee1, actialList.get(0));

        verify(storage).getAllByNameSpace("trainee");
    }

    @Test
    void createTest(){
        Trainee newtrainee = Trainee.traineeBuilder()
                .address("Dnipro")
                .isActive(false)
                .trainingsIds(List.of())
                .firstName("Svitlana")
                .lastName("Kovalchenko")
                .dateOfBirth(LocalDate.of(1986,5,30))
                .build();


        when(storage.addToStorage(eq("trainee"), any(EntityInterface.class))).thenReturn(newtrainee);

        Trainee actual = traineeDAO.create(newtrainee);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Svitlana", actual.getFirstName());
        Assertions.assertEquals("Dnipro", actual.getAddress());

        verify(storage).addToStorage("trainee",newtrainee);
    }

    @Test
    void updateTest(){
        Trainee updated = trainee1;
        updated.setTrainingsIds(List.of(1L,2L));
        updated.setActive(true);
        updated.setUserId(3L);

        when(storage.update("trainee:3",trainee1)).thenReturn(updated);

        Trainee actual = traineeDAO.update(trainee1);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isActive());
        Assertions.assertEquals(2, actual.getTrainingsIds().size());

        verify(storage).update("trainee:3",trainee1);
    }

    @Test
    void deleteTest(){

            traineeDAO.delete(1L);
            verify(storage).deleteFromStorage("trainee:1");
    }

}
