package com.alvevel;

import java.util.List;

public class DecompressionResult {
    private final List<Integer> bites;
    private final String fileName;

    public DecompressionResult(List<Integer> bites, String fileName) {
        this.bites = bites;
        this.fileName = fileName;
    }

    public void writeToFile() {

    }
}
