package com.mara.tfgcine.model.user;

import jakarta.persistence.*;

@Entity
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
    }

    public void setStatus(AccountStatus accountStatus) {
    }
}