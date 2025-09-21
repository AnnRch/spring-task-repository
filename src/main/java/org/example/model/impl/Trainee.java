package org.example.model.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.EntityInterface;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class Trainee extends User implements EntityInterface {

    @Setter
    private LocalDate dateOfBirth;

    @Setter
    private String address;

    @Setter
    private Long userId;

    @Setter
    List<Long> trainingsIds;


    private Trainee(String firstName, String lastName, String userName, String password,
                   boolean isActive,
                   LocalDate dateOfBirth, String address, Long userId, List<Long> trainingsIds
                   ){
        super(firstName, lastName, userName, password, isActive);
        this.address = address;
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.trainingsIds = trainingsIds;
    }

    @Builder(builderMethodName = "traineeBuilder")
    public static Trainee create(String firstName, String lastName, String userName, String password,
                                 boolean isActive,
                                 LocalDate dateOfBirth, String address, Long userId, List<Long> trainingsIds){

        return new Trainee(firstName, lastName, userName, password, isActive,
                            dateOfBirth, address, userId, trainingsIds);
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
        Trainee trainee = (Trainee) value;
        return create(trainee.getFirstName(), trainee.getLastName(), trainee.getUserName(),
                trainee.getPassword(), trainee.isActive, trainee.getDateOfBirth(),
                trainee.getAddress(), trainee.userId, trainee.trainingsIds
                );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Trainee trainee = (Trainee) o;
        return Objects.equals(dateOfBirth, trainee.dateOfBirth) && Objects.equals(address, trainee.address) && Objects.equals(userId, trainee.userId) && Objects.equals(trainingsIds, trainee.trainingsIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateOfBirth, address, userId, trainingsIds);
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", userId=" + userId +
                ", trainingsIds=" + trainingsIds +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }

}
