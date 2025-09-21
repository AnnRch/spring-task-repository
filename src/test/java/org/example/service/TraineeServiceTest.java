package org.example.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.dao.impl.TraineeDAO;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Training;
import org.example.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jmx.export.assembler.AbstractConfigurableMBeanInfoAssembler;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

       @Mock
       private TraineeDAO traineeDAO;

       @InjectMocks
       private TraineeServiceImpl traineeService;

       private Trainee trainee1, trainee2;

       private Map<String, EntityInterface> map = new ConcurrentHashMap<>();

       @BeforeEach
       void setUp(){
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

           map.put("trainee:1", trainee1);
           map.put("trainee:2", trainee2);
       }

       @Test
       void getByIdTest(){
            when(traineeDAO.getById(1L)).thenReturn(trainee1);

            Trainee actual = traineeService.getById(1L);
           Assertions.assertNotNull(actual);
           Assertions.assertEquals("Anna", actual.getFirstName());

           verify(traineeDAO).getById(1L);
       }

       @Test
       void getAllTest(){

            when(traineeDAO.getAll()).thenReturn(List.of(trainee1, trainee2));

            List<Trainee> actualList = traineeService.getAll();
            Assertions.assertEquals(2, actualList.size());
            Assertions.assertEquals("Anna", actualList.get(0).getFirstName());

            verify(traineeDAO).getAll();
       }

       @Test
       void createTest(){
            Trainee newTrainee = Trainee.traineeBuilder()
                    .dateOfBirth(LocalDate.of(1984,7,14))
                    .address("Melitopol")
                    .firstName("Alesya")
                    .lastName("Kalinina")
                    .trainingsIds(List.of())
                    .isActive(true)
                    .build();

            when(traineeDAO.create(any(Trainee.class))).thenReturn(newTrainee);

            Trainee actual = traineeService.create(newTrainee);

            Assertions.assertNotNull(actual);
            Assertions.assertEquals("Alesya", actual.getFirstName());

            verify(traineeDAO).create(newTrainee);

       }

       @Test
       void updateTest(){
            Trainee updated = trainee1;
            updated.setTrainingsIds(List.of(1L,2L));
            updated.setAddress("Spain");

            when(traineeDAO.update(any(Trainee.class))).thenReturn(updated);

            Trainee actual = traineeService.update(updated);

           Assertions.assertEquals(2, actual.getTrainingsIds().size());
           Assertions.assertEquals("Spain", actual.getAddress());

           verify(traineeDAO).update(updated);

       }

       @Test
       void deleteTest(){
            traineeService.deleteById(1L);
            verify(traineeDAO).delete(1L);
       }

       @Test
       void addTrainingToTraineeTest(){
         Training  training1 = Training.trainingBuilder()
                    .id(1L)
                   .date(LocalDate.of(2025,9,17))
                   .type(TrainingType.CARDIO)
                   .name("Crossfit")
                   .duration(60L)
                   .trainerId(null)
                   .traineeIds(List.of())
                   .build();

           traineeService.addTrainingToTrainee(trainee1, training1.getId());

           Assertions.assertEquals(List.of(1L), trainee1.getTrainingsIds());

       }

}
