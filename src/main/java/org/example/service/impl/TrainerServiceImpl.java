package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.impl.TrainerDAO;
import org.example.model.impl.Trainer;
import org.example.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerDAO trainerDAO;

    @Override
    public Trainer getById(Long id) {
        if(id == null){
            log.error("cannot get trainer cause id is null");
            throw new NullPointerException("null id was given");
        }


        return trainerDAO.getById(id);
    }

    @Override
    public List<Trainer> getAll() {
        return trainerDAO.getAll();
    }

    @Override
    public Trainer create(Trainer trainer) {

        if(trainer == null){
            log.error("creation failed due to null data");
            throw new NullPointerException("null trainer value");
        }

        return trainerDAO.create(trainer);
    }

    @Override
    public Trainer update(Trainer trainer) {

        if(trainer == null){
            log.error("updating failed due to null data");
            throw new NullPointerException("null trainer value");
        }


        return trainerDAO.update(trainer);
    }

    @Override
    public void deleteById(Long id) {
          if(id == null){
              log.error("can't delete because of null id value");
              throw new NullPointerException("null id value");
          }
          trainerDAO.delete(id);
    }

    @Override
    public void addTrainingToTrainer(Trainer trainer, Long trainingId) {
        if(trainer == null || trainingId == null){
            log.error("operation failed, null value were provided");
            throw new NullPointerException("null values");
        }


        List<Long> trainingsIds = trainer.getTrainingsIds() == null
                ? new ArrayList<>()
                : new ArrayList<>(trainer.getTrainingsIds());

        if(!trainingsIds.contains(trainingId)){
            trainingsIds.add(trainingId);
        }

        log.info("assigning training {} to trainer {}", trainingId, trainer.getUserId());
        trainer.setTrainingsIds(trainingsIds);

    }
}
