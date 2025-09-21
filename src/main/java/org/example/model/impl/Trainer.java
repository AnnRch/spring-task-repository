package org.example.model.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class Trainer extends User implements EntityInterface {

    @Setter
    private String specialization;

    @Setter
    private Long userId;

    @Setter
    private List<Long> trainingsIds;

    @Setter
    private List<TrainingType> trainingTypes;



    private Trainer(String firstName, String lastName, String userName, String password, boolean isActive,
                    String specialization, Long userId, List<Long> trainingsIds,
                    List<TrainingType> trainingTypes
                   ){
        super(firstName, lastName, userName, password, isActive);
        this.specialization = specialization;
        this.userId = userId;
        this.trainingsIds = trainingsIds;
        this.trainingTypes = trainingTypes;
    }

    @Builder(builderMethodName = "trainerBuilder")
    public static Trainer create(String firstName, String lastName, String userName, String password, boolean isActive,
                                 String specialization, Long userId, List<Long> trainingsIds,
                                 List<TrainingType> trainingTypes){
        return new Trainer(firstName,lastName,userName,password,isActive,specialization,
                userId,trainingsIds,trainingTypes
                );
    }

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public void setId(Long id) {
        this.userId = id;
    }

    @Override
    public EntityInterface create(EntityInterface value) {
        Trainer trainer = (Trainer) value;
        return create(trainer.getFirstName(), trainer.getLastName(), trainer.getUserName(), trainer.getPassword(),
                trainer.isActive, trainer.getSpecialization(), trainer.getUserId(), trainer.getTrainingsIds(),
                trainer.getTrainingTypes()
                );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(specialization, trainer.specialization) && Objects.equals(userId, trainer.userId) && Objects.equals(trainingsIds, trainer.trainingsIds) && Objects.equals(trainingTypes, trainer.trainingTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialization, userId, trainingsIds, trainingTypes);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization='" + specialization + '\'' +
                ", userId=" + userId +
                ", trainingsIds=" + trainingsIds +
                ", trainingTypes=" + trainingTypes +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
