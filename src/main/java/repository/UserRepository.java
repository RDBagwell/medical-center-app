package repository;

import model.PasswordHash;
import model.User;
import model.Username;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserName(Username userName);

    void add(Username userName, PasswordHash passwordHash);

    void updatePassword(int id, PasswordHash passwordHash);
}
