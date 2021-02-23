package ilia.nemankov.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ROOMS")
    private Rooms rooms;

    private Integer capacity;

    @Column(name = "FOOD")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private FoodType foodType;

    private Integer price;

}
