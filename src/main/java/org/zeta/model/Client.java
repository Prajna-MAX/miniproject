package org.zeta.model;

import org.zeta.model.Role;
import org.zeta.model.User;

public class Client extends User {

    public Client(String username, String password) {
        super(username, password, Role.CLIENT);
    }
}
