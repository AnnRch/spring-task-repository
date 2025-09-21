package org.example.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.processing.EntityProcessor;
import org.example.model.EntityInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class Storage {


    private Map<String, EntityInterface> storage = new ConcurrentHashMap<>();

    private final EntityProcessor entityProcessor;

    public void putAll(Map<String, EntityInterface> entityMap){
        storage.putAll(entityMap);
    }

    private Long generateId(Map<String, EntityInterface> storage, String nameSpace){
        return storage.keySet().stream()
                .filter(key -> key.startsWith(nameSpace))
                .mapToLong(key -> Long.parseLong(key.substring(key.indexOf(":") + 1)))
                .reduce(0L, (acc, value) -> {
                    if (value > acc)
                        acc = value;
                    return acc + 1;
                });
    }

    public EntityInterface getById(String nameSpace, Long id){
            return storage.get(nameSpace + ":" + id);
    }

    public EntityInterface addToStorage(String key, EntityInterface value){
        System.out.println(value);
        EntityInterface entity = entityProcessor.create(storage, key, value);

        if(entity == null){
            log.error("creation failed due to wrong key");
            throw  new NullPointerException("null value returned due to wrong key");
        }

        log.info("generating id for entity {}", entity);
        entity.setId(generateId(storage, key));
        storage.put(key + ":" + entity.getId(), entity);
        return entity;

    }

    public EntityInterface update(String key, EntityInterface value){
        return entityProcessor.update(storage, key, value);
    }

    public void deleteFromStorage(String key){
        if(key == null){
            log.error("deleting failed due to null key");
            throw new NullPointerException("null key");
        }

        log.info("removing value with key {} from storage ",key);
        storage.remove(key);
    }

    public Map<String, EntityInterface> getStorage(){
        return new ConcurrentHashMap<>(storage);
    }

    public List<EntityInterface> getAllByNameSpace(String nameSpace){
        log.info("listing entities by namespace {} :", nameSpace);
        return storage.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(nameSpace))
                .map(Map.Entry::getValue)
                .toList();
    }
}
