package org.zeta.model;

import java.util.UUID;

public class User {

    private String id;
    private String name;
    private String password;
    private ROLE role;


    public User() {
    }

    public User(String name, String password, ROLE role) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }
}
