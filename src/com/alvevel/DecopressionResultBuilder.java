package com.alvevel;

import java.util.ArrayList;
import java.util.List;

public class DecopressionResultBuilder {
    private String fileName;
    private List<Integer> bytes = new ArrayList<>();
    private DecompressionResult decompressionResult;


    public DecopressionResultBuilder addBit(Bit bit) {

        return this;
    }

    public DecompressionResult build() {
        return null;
    }
}
