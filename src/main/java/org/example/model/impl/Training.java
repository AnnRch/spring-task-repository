package org.example.model.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.EntityInterface;
import org.example.model.enums.TrainingType;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class Training  implements EntityInterface {

    private Long id;

    @Setter
    private List<Long> traineeIds;

    @Setter
    private Long trainerId;

    @Setter
    private String name;

    @Setter
    private TrainingType trainingType;

    @Setter
    private Long duration;

    @Setter
    private LocalDate date;



    private Training(Long id, List<Long> traineeIds, Long trainerId, String name,
                    TrainingType type, Long duration, LocalDate date
                    ){
        this.id = id;
        this.traineeIds = traineeIds;
        this.trainerId = trainerId;
        this.name = name;
        this.date = date;
        this.trainingType = type;
        this.duration = duration;
    }

    @Builder(builderMethodName = "trainingBuilder")
    public static Training create(Long id, List<Long> traineeIds, Long trainerId, String name,
                                  TrainingType type, Long duration, LocalDate date){

        return new Training(id, traineeIds, trainerId, name, type, duration, date);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public EntityInterface create(EntityInterface value) {
        Training training = (Training) value;
        return create(training.getId(), training.getTraineeIds(), training.getTrainerId(),
                training.getName(), training.getTrainingType(), training.getDuration(),
                training.getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Objects.equals(id, training.id) && Objects.equals(traineeIds, training.traineeIds) && Objects.equals(trainerId, training.trainerId) && Objects.equals(name, training.name) && trainingType == training.trainingType && Objects.equals(duration, training.duration) && Objects.equals(date, training.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, traineeIds, trainerId, name, trainingType, duration, date);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", traineeIds=" + traineeIds +
                ", trainerId=" + trainerId +
                ", name='" + name + '\'' +
                ", trainingType=" + trainingType +
                ", duration=" + duration +
                ", date=" + date +
                '}';
    }
}
