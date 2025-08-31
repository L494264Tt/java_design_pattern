package com.example.design_pattern.decoration_pattern.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

/**
 * @author L494264Tt@outlook.com
 * @date 2025/8/31 14:19
 */

public class Main {
    public static void main(String[] args) {
        File file = new File("src/test.pdf");
        long l = Instant.now().toEpochMilli();
        try (bufferedFileInputStream bis = new bufferedFileInputStream(new FileInputStream(file))){
            while(true){
                int bufferRead = bis.read();
                if(bufferRead == -1){
                    break;
                }
            }
            System.out.println("用时"+ (Instant.now().toEpochMilli()-l) + "毫秒");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
