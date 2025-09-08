package com.example.design_pattern.itreator_pattern;


import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("src/demo.user");

        UserFile users = new UserFile(file);
        for (User user : users) {
            System.out.println(user);
        }

    }
}
