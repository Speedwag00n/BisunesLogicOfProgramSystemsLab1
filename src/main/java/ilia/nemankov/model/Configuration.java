package ilia.nemankov.model;

import lombok.Data;

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
    private FoodType foodType;

    private Integer price;

}
