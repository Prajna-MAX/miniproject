package org.zeta.service.implementation;

import org.zeta.dao.UserDao;
import org.zeta.model.enums.Role;
import org.zeta.model.User;
import org.zeta.service.interfaces.IAuthenticationService;
import org.zeta.validation.UserValidator;
import org.zeta.validation.ValidationException;
import java.util.Optional;
import java.util.logging.Logger;

public class AuthenticationService implements IAuthenticationService {
    private final UserDao userDao;
    private static final Logger logger =
            Logger.getLogger(AuthenticationService.class.getName());

    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean register(String username,
                            String password,
                            String confirmPassword,
                            Role role) {
        try {
            UserValidator.validateRegistration(username, password, confirmPassword, role);
            if (userDao.findByUsername(username).isPresent()) {
                throw new ValidationException("User already exists.");
            }
            User user = new User(username, password, role);
            userDao.save(user);
            logger.info("Registration successful!");
            return true;
        } catch (ValidationException e) {
            logger.warning(e.getMessage());
            return false;
        }
    }


    @Override
    public User login(String username, String password) {
        UserValidator.validateLogin(username, password);
        Optional<User> userOpt = userDao.findByUsername(username);
        if (userOpt.isPresent()
                && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        throw new ValidationException("Invalid username or password.");
    }
}
