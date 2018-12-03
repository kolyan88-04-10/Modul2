package com.alvevel;

import java.util.ArrayList;
import java.util.List;

public class CompressionResultBuilder implements Builder {
    private String fileName;
    private Node huffmanTree;
    private List<Integer> compressionBytes = new ArrayList<>();
    private int oneByte;
    private int position;

    public CompressionResultBuilder() {}

    public CompressionResultBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setHuffmanTree(Node huffmanTree) {
        this.huffmanTree = huffmanTree;
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

    public Result build() {
        int remainder = position % 8;
        if (remainder != 0) {
            oneByte <<= (8 - remainder);
        }
        compressionBytes.add(oneByte);
        return new CompressionResult(
                compressionBytes, position,
                fileName, huffmanTree);
    }
}
