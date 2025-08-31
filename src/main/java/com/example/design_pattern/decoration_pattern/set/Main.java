package com.example.design_pattern.decoration_pattern.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author L494264Tt@outlook.com
 * @date 2025/8/31 13:47
 */

public class Main {
    public static void main(String[] args) {
        Set<String> historySet = new HistorySet<>(new HashSet<>());

        Set<String> set = new HistorySet<>(historySet);
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");
        set.remove("4");
        set.remove("4");
        set.remove("5");
        set.remove("1");
        System.out.println(set);

    }
}
