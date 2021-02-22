package ilia.nemankov.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "HOTEL")
    private Hotel hotel;

    private String name;

    private Integer area;

    @Column(name = "ROOMS_NUMBER")
    private Integer roomsNumber;

}
