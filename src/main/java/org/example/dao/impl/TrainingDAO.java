package org.example.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.DAO;
import org.example.model.impl.Training;
import org.example.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class TrainingDAO implements DAO<Training> {

    @Autowired
    private Storage storage;

    @Override
    public Training getById(Long id) {
        return (Training) storage.getById("training", id);
    }

    @Override
    public List<Training> getAll() {
        return storage.getAllByNameSpace("training").stream()
                .map(item -> (Training) item)
                .toList();
    }

    @Override
    public Training create(Training training) {
        return (Training) storage.addToStorage("training", training);
    }

    @Override
    public Training update(Training training) {
        return (Training) storage.update("training:" + training.getId(), training);
    }

    @Override
    public void delete(Long id) {
        if(id == null){
            log.error("deleting failed, null id");
            throw new NullPointerException("null id");
        }

        storage.deleteFromStorage("training:" + id);
    }
}
