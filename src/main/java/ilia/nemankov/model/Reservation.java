package ilia.nemankov.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "HOTEL")
    private Hotel hotel;

    private String name;

    private String surname;

    private String email;

    @Column(name = "ARRIVAL_DATE")
    private Date arrivalDate;

    @Column(name = "DEPARTURE_DATE")
    private Date departureDate;

}
