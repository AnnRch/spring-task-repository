package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.impl.TraineeDAO;
import org.example.model.impl.Trainee;
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    private TraineeDAO traineeDAO;

    @Override
    public Trainee getById(Long id) {


        if(id == null){
            log.error("can't get trainee, null id was given");
            throw new NullPointerException("null id was provided");
        }

        return traineeDAO.getById(id);
    }

    @Override
    public List<Trainee> getAll() {
        return traineeDAO.getAll();
    }

    @Override
    public Trainee create(Trainee trainee) {

        if(trainee == null){
            throw  new NullPointerException("null value was provided");
        }

        return traineeDAO.create(trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {

        if(trainee == null){
            throw new NullPointerException("null value was provided");
        }

        return traineeDAO.update(trainee);
    }

    @Override
    public void deleteById(Long id) {
        if(id == null){
            throw new NullPointerException("null id was provided");
        }

        traineeDAO.delete(id);
    }

    @Override
    public void addTrainingToTrainee(Trainee trainee, Long trainingId) {
        if(trainee == null || trainingId == null){
            throw new NullPointerException("null values");
        }


        List<Long> trainingsIds = trainee.getTrainingsIds() == null
                ? new ArrayList<>()
                : new ArrayList<>(trainee.getTrainingsIds());

        if(!trainingsIds.contains(trainingId)){
           trainingsIds.add(trainingId);
           trainee.setTrainingsIds(trainingsIds);
        }
    }
}
