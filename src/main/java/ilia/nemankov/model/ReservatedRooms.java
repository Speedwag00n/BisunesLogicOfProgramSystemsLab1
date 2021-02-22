package ilia.nemankov.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ReservatedRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "RESERVATION")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "ROOMS")
    private Rooms rooms;

    @ManyToOne
    @JoinColumn(name = "CONFIGURATION")
    private Configuration configuration;

    @Column(name = "ROOMS_NUMBER")
    private Integer roomsNumber;

}
