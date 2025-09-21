package org.example.dao.impl;

//import jdk.internal.vm.annotation.Contended;
import org.example.dao.DAO;
import org.example.model.impl.Trainer;
import org.example.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerDAO implements DAO<Trainer> {

    @Autowired
    private Storage storage;

    @Override
    public Trainer getById(Long id) {

        return (Trainer) storage.getById("trainer", id);
    }

    @Override
    public List<Trainer> getAll() {
        return storage.getAllByNameSpace("trainer").stream()
                .map(value -> (Trainer) value)
                .toList();
    }

    @Override
    public Trainer create(Trainer trainer) {
        return (Trainer) storage.addToStorage("trainer", trainer);
    }

    @Override
    public Trainer update(Trainer trainer) {

        return (Trainer) storage.update("trainer:" + trainer.getUserId(), trainer);
    }

    @Override
    public void delete(Long id) {
        storage.deleteFromStorage("trainer:" + id);
    }
}
