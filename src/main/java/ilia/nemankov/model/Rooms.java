package ilia.nemankov.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "ROOMS_NUMBER")
    private Integer roomsNumber;

    @OneToMany(mappedBy = "rooms", cascade = CascadeType.ALL)
    private List<Configuration> configurations;

}
