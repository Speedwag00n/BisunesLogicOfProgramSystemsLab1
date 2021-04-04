package ilia.nemankov.model;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class User {

    private long id;

    private String login;

    private String password;

    private Date lastLogout;

    private Set<String> roles;

}
