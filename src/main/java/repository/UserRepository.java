package repository;

import model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserName(String userName);

    void add(String userName, String passwordHash);

    void updatePassword(int id, String passwordHash);
}
