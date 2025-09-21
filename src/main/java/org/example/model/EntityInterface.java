package org.example.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.model.impl.Trainee;
import org.example.model.impl.Trainer;
import org.example.model.impl.Training;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Trainer.class, name = "trainer"),
        @JsonSubTypes.Type(value = Trainee.class, name = "trainee"),
        @JsonSubTypes.Type(value = Training.class, name = "training")
})
public interface EntityInterface {
    Long getId();
    void setId(Long id);
    EntityInterface create(EntityInterface value);
}
