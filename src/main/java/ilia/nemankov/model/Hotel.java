package ilia.nemankov.model;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Data
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "DIRECTION")
    private Direction direction;

    private String address;

    @Column(name = "HOTEL_TYPE")
    private HotelType hotelType;

    private String description;

}
