package com.mara.tfgcine.model.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public void setRole(Role role) {
        this.role = role;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}