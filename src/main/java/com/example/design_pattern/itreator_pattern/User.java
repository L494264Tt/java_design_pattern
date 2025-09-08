package com.example.design_pattern.itreator_pattern;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class User{

    private String name;

    private Integer age;


    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }


}
