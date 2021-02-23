package ilia.nemankov.model;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "CITY")
    private City city;

    private String address;

    @Column(name = "HOTEL_TYPE")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private HotelType hotelType;

    private String description;

    private Integer stars;

    @OneToMany(mappedBy = "hotel")
    private List<Rooms> rooms;

}
