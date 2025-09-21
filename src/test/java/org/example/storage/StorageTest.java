package org.example.storage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StorageTest {

    @Mock
    private EntityProcessor entityProcessor;

    @InjectMocks
    private Storage storage;

    private Map<String, EntityInterface> map = new ConcurrentHashMap<>();
    private Trainer trainer;
    private Trainee trainee;
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


        map.put("trainee:1", trainee);
        map.put("trainer:1",trainer);
        map.put("training:1", training);

    }

    @Test
    void putAllTest(){

        storage.putAll(map);
        Map<String, EntityInterface> actualMap = storage.getStorage();

        Assertions.assertEquals(3, actualMap.size());
        Assertions.assertSame(trainee, actualMap.get("trainee:1"));

    }

    @Test
    void getById(){

        storage.putAll(map);
        EntityInterface actual = storage.getById("trainer", 1L);
        Assertions.assertSame(trainer, actual);
    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @ParameterizedTest
    @MethodSource("testCases")
    void addToStorageTest(){
        storage.putAll(map);

        String key = "trainer:1";
        EntityInterface param = mock(EntityInterface.class);
        EntityInterface value = mock(EntityInterface.class);

        when(entityProcessor.create(any(),eq(key),eq(param))).thenReturn(value);

        doNothing().when(value).setId(anyLong());

        when(value.getId()).thenReturn(1L);

        EntityInterface actual = storage.addToStorage(key, param);

        verify(entityProcessor).create(any(), eq(key), eq(param));
        verify(value).setId(anyLong());

        Assertions.assertEquals(value, actual);
    }


    private static Stream<Arguments> testCases(){
        EntityInterface param1 = mock(EntityInterface.class);
        EntityInterface param2 = mock(EntityInterface.class);
        EntityInterface param3 = mock(EntityInterface.class);


        return Stream.of(
                Arguments.of("training:10",param1),
                Arguments.of("training:2",param2),
                Arguments.of("trainee:3", param3)
        );

    }

    @ParameterizedTest
    @MethodSource("wrongTestInputs")
    void addToStorageWrongKeyCase(){

        String key = "some-key";
        EntityInterface param = mock(EntityInterface.class);

        when(entityProcessor.create(anyMap(), eq(key), eq(param))).thenReturn(null);

        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> storage.addToStorage(key, param)
        );

        Assertions.assertEquals("null value returned due to wrong key", exception.getMessage());
    }


    private static Stream<Arguments> wrongTestInputs(){

        EntityInterface param1 = mock(EntityInterface.class);
        EntityInterface param2 = mock(EntityInterface.class);
        EntityInterface param3 = mock(EntityInterface.class);

        return Stream.of(
                Arguments.of("wrong-key-1",param1),
                Arguments.of("wrong-key-2",param2),
                Arguments.of("5657ghjg", param3)
        );

    }

    @Test
    void updateTest(){

        String key = "trainer:1";
        EntityInterface param = mock(EntityInterface.class);
        EntityInterface value = mock(EntityInterface.class);

        when(entityProcessor.update(anyMap(),eq(key),eq(param))).thenReturn(value);
        EntityInterface actual = storage.update(key, param);

        verify(entityProcessor).update(anyMap(), eq(key), eq(param));

        Assertions.assertEquals(value, actual);
    }

    @Test
    void deleteFromStorageTest(){
        storage.putAll(map);
        storage.deleteFromStorage("trainee:1");

        Map<String, EntityInterface> actualMap = storage.getStorage();
        Assertions.assertEquals(2, actualMap.size());
        Assertions.assertFalse(actualMap.containsKey("trainee:1"));
    }

    @Test
    void getStorageTest(){
        storage.putAll(map);

        Map<String, EntityInterface> actualMap = storage.getStorage();
        Assertions.assertEquals(3, actualMap.size());
        Assertions.assertEquals(trainee, actualMap.get("trainee:1"));
    }

    @Test
    void getAllByNameSpaceTest(){
        storage.putAll(map);
        List<EntityInterface> actualList = storage.getAllByNameSpace("trainee");

        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals(trainee, actualList.get(0));
    }

}
