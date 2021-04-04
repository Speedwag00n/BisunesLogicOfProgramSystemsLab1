package ilia.nemankov.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Convenience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CONFIGURATION_CONVENIENCE",
            joinColumns = @JoinColumn(name = "CONVENIENCE"),
            inverseJoinColumns = @JoinColumn(name = "CONFIGURATION")
    )
    private List<Configuration> configurations;

}
