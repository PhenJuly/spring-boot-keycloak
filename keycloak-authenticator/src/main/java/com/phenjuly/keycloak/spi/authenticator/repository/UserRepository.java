package com.phenjuly.keycloak.spi.authenticator.repository;

import com.phenjuly.keycloak.spi.authenticator.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author PhenJuly
 */
public class UserRepository {

    private List<User> users;

    // TODO 连接数据库
    public UserRepository() {
        users = Arrays.asList(
                new User("1","Katie" ,"Katie", "Washington"),
                new User("2",  "Enrique","Enrique", "Perkins"),
                new User("3","Joshua", "Joshua", "Little"),
                new User("4","Billie", "Billie", "Newman"),
                new User("5", "Leslie","Leslie", "Thompson"),
                new User("6","spi-puppet", "spi", "puppet")
        );
    }

    public List<User> getAllUsers() {
        return users;
    }

    public int getUsersCount() {
        return users.size();
    }

    public User findUserById(String id) {
        return users.stream().filter(user -> user.getUserId().equals(id)).findFirst().get();
    }

    public User findUserByUsernameOrEmail(String username) {
        return users.stream()
                .filter(user -> user.getUserName().equalsIgnoreCase(username) || user.getEmail().equalsIgnoreCase(username))
                .findFirst().get();
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
