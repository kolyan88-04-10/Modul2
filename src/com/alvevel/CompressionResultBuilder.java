package com.alvevel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompressionResultBuilder {
    private String fileName;
    private Map<Integer, String> huffmanTable;
    private List<Integer> compressionBytes = new ArrayList<>();
    private CompressionResult compressionResult;
    private int oneByte;
    private int position;

    CompressionResultBuilder() {}

    public CompressionResultBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public static void main(String[] args) {
    }

    public void setHuffmanTable(Map<Integer, String> huffmanTable) {
        this.huffmanTable = huffmanTable;
    }

    public CompressionResultBuilder addBit(Bit bit) {
        oneByte |= bit.value();
        if ((position + 1) % 8 == 0) {
            compressionBytes.add(oneByte);
            oneByte = 0;
        } else {
            oneByte <<= 1;
        }
        position++;
        return this;
    }

    public Bit getBit(int source, int digit) {
        return null;
    }

    public int writeBit(int source, int digit) {
        return 0;
    }

    public CompressionResult build() throws IOException {
        int remainder = position % 8;
        if (remainder != 0) {
            oneByte <<= (8 - remainder);
        }
        compressionBytes.add(oneByte);
        return new CompressionResult(
                compressionBytes, position,
                fileName, huffmanTable);
    }
}
