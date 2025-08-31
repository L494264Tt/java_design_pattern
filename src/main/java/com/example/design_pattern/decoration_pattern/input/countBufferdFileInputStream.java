package com.example.design_pattern.decoration_pattern.input;

import java.io.FileInputStream;

/**
 * @author L494264Tt@outlook.com
 * @date 2025/8/31 15:47
 */

public class countBufferdFileInputStream extends bufferedFileInputStream{

    private int readCount = 0;


    public countBufferdFileInputStream(FileInputStream fileInputStream) {
        super(fileInputStream);
    }



}
