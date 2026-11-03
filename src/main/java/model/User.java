package model;

public record User(int id, Username userName, PasswordHash passwordHash) {
    @Override
    public String toString() {
        return "User[id=" + id + ", userName=" + userName + "]";
    }
}
