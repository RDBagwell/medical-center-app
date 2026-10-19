package servlet;

import helper.PasswordHasher;

public class Junk {
    public static void main(String[] args) {
        String hashed = PasswordHasher.hash("password123");
        String hashed2 = PasswordHasher.hash("password123");
        String hashed3 = PasswordHasher.hash("password123");
        System.out.println(hashed);
        System.out.println(hashed2);
        System.out.println(hashed3);
    }
}
