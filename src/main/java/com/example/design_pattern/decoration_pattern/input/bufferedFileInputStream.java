package com.example.design_pattern.decoration_pattern.input;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author L494264Tt@gmail.com
 * @date 2025/8/31 15:09
 */

public class bufferedFileInputStream extends InputStream {

    private final byte[] buffer = new byte[8192];

    private int position = -1;

    private int capacity = -1;

    private final FileInputStream fileInputStream;

    public bufferedFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    @Override
    public int read() throws IOException {
        if (bufferCanRead()) {
            return readFromBuffer();
        }
        refreshBuffer();
        if (!bufferCanRead()) {
            return -1;
        }
        return readFromBuffer();
    }

    private int readFromBuffer() {
        return buffer[position++] & 0xFF;
    }

    private void refreshBuffer() throws IOException {
        capacity = fileInputStream.read(buffer);
        position = 0;
    }

    private boolean bufferCanRead() {
        if (capacity == -1) {
            return false;
        }
        if (position == capacity) {
            return false;
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
