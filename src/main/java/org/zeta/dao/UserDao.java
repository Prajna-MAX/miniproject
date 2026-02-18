package org.zeta.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import org.zeta.model.Project;
import org.zeta.model.Role;
import org.zeta.model.User;

import java.util.List;
import java.util.Optional;

public class UserDao extends BaseDao<User> {

    public UserDao() {
        super("users.json", new TypeReference<List<User>>() {});
    }

    public void save(User user) {
        add(user);
    }

    public void delete(User user) {
        remove(user);
    }

    public  Optional<User> findById(String id) {
        return dataList.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }
    public Optional<User> findIdbyName(String name) {
        return dataList.stream()
                .filter(u -> u.getUsername().equals(name))
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return dataList.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public  List<User> findByRole(Role role) {
        return dataList.stream()
                .filter(u -> u.getRole().name().equalsIgnoreCase(String.valueOf(role)))
                .toList();
    }

    public void update(User updatedUser) {
        findById(updatedUser.getId()).ifPresent(existing -> {
            dataList.remove(existing);
            dataList.add(updatedUser);
            saveToFile();
        });
    }
}
