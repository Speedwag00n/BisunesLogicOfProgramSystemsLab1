package ilia.nemankov.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    @Column(name = "LAST_LOGOUT")
    private Date lastLogout;

    private String roles;

    private Integer bonuses;

}
