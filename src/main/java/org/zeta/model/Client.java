package org.zeta.model;


public class Client extends User {

    public Client(String username, String password) {
        super(username, password, Role.CLIENT);
    }
}
