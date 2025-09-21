package org.example.dao.impl;

import org.example.dao.DAO;
import org.example.model.impl.Trainee;
import org.example.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TraineeDAO implements DAO<Trainee> {

    @Autowired
    private Storage storage;

    @Override
    public Trainee getById(Long id) {
        return (Trainee) storage.getById("trainee", id);
    }

    @Override
    public List<Trainee> getAll() {
        return storage.getAllByNameSpace("trainee").stream()
                .map(value -> (Trainee) value)
                .toList();
    }

    @Override
    public Trainee create(Trainee trainee) {
        return (Trainee) storage.addToStorage("trainee", trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {
        return (Trainee) storage.update("trainee:" + trainee.getUserId(), trainee);
    }

    @Override
    public void delete(Long id) {
        storage.deleteFromStorage("trainee:" + id);
    }
}
