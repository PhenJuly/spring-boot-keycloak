package com.phenjuly.keycloak.spi.user.storage.repository;

import com.phenjuly.keycloak.spi.user.storage.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author PhenJuly
 */
public class UserRepository {

    private List<User> users;

    // TODO 连接数据库
    public UserRepository() {
        users = Arrays.asList(
                new User("1", "Katie", "Katie", "Washington"),
                new User("2", "Enrique", "Enrique", "Perkins"),
                new User("3", "Joshua", "Joshua", "Little"),
                new User("4", "Billie", "Billie", "Newman"),
                new User("5", "Leslie", "Leslie", "Thompson"),
                new User("6", "spi-puppet", "spi", "puppet")
        );
    }

    public List<User> getAllUsers() {
        return users;
    }

    public int getUsersCount() {
        return users.size();
    }

    public User findUserById(String id) {
        Optional<User> first = users.stream().filter(user -> user.getUserId().equals(id))
                .findFirst();
        return first.isPresent() ? first.get() : null;
    }

    public User findUserByUsernameOrEmail(String username) {
        Optional<User> first = users.stream()
                .filter(user -> user.getUserName().equalsIgnoreCase(username) || user.getEmail().equalsIgnoreCase(username))
                .findFirst();
        return  first.isPresent() ? first.get() : null;
    }

    public List<User> findUsers(String query) {
        return users.stream()
                .filter(user -> user.getUserName().contains(query) || user.getEmail().contains(query))
                .collect(Collectors.toList());
    }

    public boolean validateCredentials(String username, String password) {
        return findUserByUsernameOrEmail(username).getPassword().equals(password);
    }

    public boolean updateCredentials(String username, String password) {
        findUserByUsernameOrEmail(username).setPassword(password);
        return true;
    }

}
