package ilia.nemankov.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
public class Recommendation implements Serializable {

    @EmbeddedId
    private RecommendationId id;

}
