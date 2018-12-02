package com.alvevel;

import java.util.ArrayList;
import java.util.List;

public class DecopressionResultBuilder {
    Node root;
    Node cutrrent;
    private String fileName;
    private List<Integer> bytes = new ArrayList<>();
    private DecompressionResult decompressionResult;

    public DecopressionResultBuilder(Node root, String fileName) {
        this.root = root;
        this.fileName = fileName;
        cutrrent = root;
    }

    public DecopressionResultBuilder addBit(Bit bit) {
        Node nextNode = cutrrent.next(bit);
        if (!nextNode.hasNext()) {
            bytes.add(nextNode.getValue());
            cutrrent = root;
        } else {
            cutrrent = nextNode;
        }
        return this;
    }

    public DecompressionResult build() {
        DecompressionResult decompressionResult = new DecompressionResult(bytes, fileName);
        return decompressionResult;
    }
}
