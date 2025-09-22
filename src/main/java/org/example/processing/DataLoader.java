package org.example.processing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.EntityInterface;
import org.example.storage.Storage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements BeanPostProcessor{

    private final ObjectProvider<EntityProcessor> entityProcessorProvider;

    private Map<String, EntityInterface> entityMap = new ConcurrentHashMap<>();

    @Value("${data.file.path}")
    private String filePath;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Storage){
            EntityProcessor entityProcessor = entityProcessorProvider.getIfAvailable();

            if(entityProcessor != null){

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());

                String fileName = Paths.get(filePath).getFileName().toString();

                try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)){

                    if(inputStream == null){
                        log.warn("data.json not found in classpath");
                        return bean;
                    }

                    log.debug("reading data from data.json file");
                    entityMap = mapper.readValue(
                            inputStream,
                            new TypeReference<>() {
                            }
                    );


                    for(Map.Entry<String, EntityInterface> entry: entityMap.entrySet()){

                        log.debug("processing entity for key {} ", entry.getKey());
                        entityMap.put(entry.getKey(), entityProcessor.create(entityMap, entry.getKey() ,entry.getValue()));

                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                log.info("saving read data to storage");
                ((Storage) bean).putAll(entityMap);
            }

            else {
                log.error("entytiPerocessor bean is not available");
            }
        }
        return bean;
    }
}
