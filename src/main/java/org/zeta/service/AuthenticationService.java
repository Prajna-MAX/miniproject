package org.zeta.service;
import org.zeta.dao.UserDao;
import org.zeta.model.Role;
import org.zeta.model.User;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AuthenticationService {

    private UserDao UserDao;
    private static final Logger logger =
            Logger.getLogger(AuthenticationService.class.getName());

    public AuthenticationService(UserDao UserDao) {
        this.UserDao = UserDao;
    }

    public boolean register(String username, String password, String confirmPassword, Role role) throws IOException {

        if (!password.equals(confirmPassword)) {
            logger.warning("Passwords do not match!");
            return false;
        }

        if (UserDao.findByUsername(username)==null) {
            logger.warning("User already exists!");
            return false;
        }

        User user = new User(username, password, role);
        UserDao.save(user);

        logger.info("Registration successful!");
        return true;
    }

    public User login(String username, String password) {

        List<User> users = UserDao.getAll();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

}
