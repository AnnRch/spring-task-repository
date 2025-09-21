package org.example;

import org.example.config.Config;
import org.example.facade.TrainingFacade;
import org.example.model.enums.TrainingType;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);

//     beans
        TrainingFacade trainingFacade = applicationContext.getBean(TrainingFacade.class);
        TraineeService traineeService  = applicationContext.getBean(TraineeService.class);
        TrainerService trainerService = applicationContext.getBean(TrainerService.class);
        TrainingService trainingService = applicationContext.getBean(TrainingService.class);


        Trainer trainer = trainerService.getById(1L);
        Training current = trainingService.getById(3L);
        current = trainingFacade.createTraining(trainer, current);

        System.out.println("---------------------------".repeat(1000));
        System.out.println("current training " + current);

        System.out.println("trainings by " + trainer.getFirstName() + " " + trainer.getLastName());
        System.out.println(trainingFacade.getAllTrainingsByTrainer(trainer));

        Trainee trainee = traineeService.getById(1L);
        current = trainingFacade.enrollForTraining(trainee, trainingService.getById(3L));

//enroll trainee for training
        System.out.println("-----------------------------".repeat(1000));
        System.out.println("enrolled trainees : ");
        System.out.println(trainingFacade.getAllEnrolledTrainees(current));

        System.out.println(current);

//listing trainings by date
        System.out.println("-----------------------------".repeat(1000));
        System.out.println("trainings by date : ");
        System.out.println( trainingFacade.getAllTrainingsByDate(LocalDate.of(2025,9,10)));;

//withdraw trainee from training
        System.out.println("-------------------------".repeat(1000));
        trainingFacade.withdrawFromTraining(trainee,current);
        System.out.println("withdrawn trainee : " + trainee);
        System.out.println(current);

//        remove training from trainer
        System.out.println("------------------------".repeat(1000));
        trainingFacade.removeTrainingFromTrainer(trainer, current);
        System.out.println(trainer);
        System.out.println(current);


//        crating new trainers
        Trainer newTrainer = Trainer.trainerBuilder()
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.CALISTHENICS))
                .firstName("Svitlana")
                .lastName("Kovalchuk")
                .isActive(true)
                .build();

        System.out.println(trainerService.create(newTrainer));


        Trainer newTrainer1 = Trainer.trainerBuilder()
                .trainingTypes(List.of(TrainingType.CARDIO, TrainingType.CALISTHENICS))
                .firstName("Svitlana")
                .lastName("Stoikova")
                .isActive(true)
                .build();


        System.out.println(trainerService.create(newTrainer1));
        System.out.println(trainerService.getAll());


//        deleting trainer
        trainerService.deleteById(7L);
        System.out.println(trainerService.getAll());


        System.out.println( "Hello World!" );

    }
}
