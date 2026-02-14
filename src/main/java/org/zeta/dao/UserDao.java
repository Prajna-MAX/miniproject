package org.zeta.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import org.zeta.model.User;

import java.util.List;
import java.util.Optional;

public class UserDao extends BaseDao<User> {

    public UserDao(String fileName) {
        super(fileName, new TypeReference<List<User>>() {});
    }

    public void save(User user) {
        add(user);
    }

    public void delete(User user) {
        remove(user);
    }

    public Optional<User> findById(String id) {
        return dataList.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return dataList.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public List<User> findByRole(String role) {
        return dataList.stream()
                .filter(u -> u.getRole().name().equalsIgnoreCase(role))
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
