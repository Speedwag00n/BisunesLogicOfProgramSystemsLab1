package ilia.nemankov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RecommendationId implements Serializable {

    @Column(name = "target_user")
    private long userId;

    private long hotel;

}
