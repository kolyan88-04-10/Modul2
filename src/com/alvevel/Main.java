package com.alvevel;

public class Main {

    public static void main(String[] args) {
        Compressor.compress("test.txt", "test");
        Decompressor.decompress("test.hcf", "test.ht", "testUnpack.txt");
    }

}
