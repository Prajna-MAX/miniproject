package org.zeta.model;

public class Builder extends User {

    public Builder(String username, String password) {
        super(username, password, Role.BUILDER);
    }
}

