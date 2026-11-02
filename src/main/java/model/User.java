package model;

public record User(int id, String userName, String passwordHash) {
    @Override
    public String toString() {
        return "User[id=" + id + ", userName=" + userName + "]";
    }
}
