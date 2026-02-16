package org.zeta.service.interfaces;

import org.zeta.model.Role;
import org.zeta.model.User;

public interface IAuthenticationService {

    boolean register(String username, String password,
                     String confirmPassword, Role role);

    User login(String username, String password);
}
