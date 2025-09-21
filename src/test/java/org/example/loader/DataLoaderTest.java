package org.example.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.EntityInterface;
import org.example.processing.DataLoader;
import org.example.processing.EntityProcessor;
import org.example.storage.Storage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.ObjectProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataLoaderTest {

    @Mock
    private EntityProcessor entityProcessor;

    @Mock
    private Storage storage;

    @InjectMocks
    private DataLoader dataLoader;


    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    void postProcessBeforeInitializationTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test-data.json");
        Assertions.assertNotNull(inputStream, "test file not found");

        Map<String, EntityInterface> originalMap = mapper.readValue(
                inputStream,
                new TypeReference<>() {});

        Map<String, EntityInterface> workMap = new ConcurrentHashMap<>();

        for(Map.Entry<String, EntityInterface> entry: originalMap.entrySet()){
            String key = entry.getKey();
            EntityInterface value = entry.getValue();
            EntityInterface entity = mock(EntityInterface.class);

            when(entityProcessor.create(originalMap, key, value)).thenReturn(entity);
            workMap.put(key, entity);
        }

        ObjectProvider<EntityProcessor> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(entityProcessor);

        DataLoader dataLoader1 = new DataLoader(provider);

        Object actual = dataLoader1.postProcessBeforeInitialization(storage, "storage");

        verify(storage).putAll(anyMap());
        Assertions.assertSame(storage, actual);
    }


}


