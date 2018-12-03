package com.alvevel;

import java.util.ArrayList;
import java.util.List;

public class DecopressionResultBuilder {
    private Node root;
    private Node current;
    private String fileName;
    private List<Integer> bytes = new ArrayList<>();

    public DecopressionResultBuilder(Node root, String fileName) {
        this.root = root;
        this.fileName = fileName;
        current = root;
    }

    public DecopressionResultBuilder addBit(Bit bit) {
        Node nextNode = current.next(bit);
        if (!nextNode.hasNext()) {
            bytes.add(nextNode.getValue());
            current = root;
        } else {
            current = nextNode;
        }
        return this;
    }

    public DecompressionResult build() {
        return new DecompressionResult(bytes, fileName);
    }
}
