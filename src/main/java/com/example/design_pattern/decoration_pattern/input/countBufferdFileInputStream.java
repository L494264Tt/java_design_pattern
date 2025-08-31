package com.example.design_pattern.decoration_pattern.input;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author L494264Tt@outlook.com
 * @date 2025/8/31 15:47
 */

public class countBufferdFileInputStream extends bufferedFileInputStream{

    private int readCount = 0;

    public countBufferdFileInputStream(FileInputStream fileInputStream) {
        super(fileInputStream);
    }

    @Override
    public int read() throws IOException {
        int result = super.read();
        if (result != -1){
            readCount++;
        }
        return result;
    }
    public int getReadCount(){
        return readCount;
    }



}
