package com.alvevel;

public class Main {

    public static void main(String[] args) {
        Compressor.compress("readme.txt", "readme");
        Decompressor.decompress("readme.hcf", "readme.ht", "readmeUnpack.txt");
    }

}
