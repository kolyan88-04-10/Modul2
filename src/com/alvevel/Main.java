package com.alvevel;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.alvevel.Bit.ONE;
import static com.alvevel.Bit.ZERO;
import static com.alvevel.CompressionResult.*;

public class Main {

    public static void main(String[] args) {
//        Bit[] bits = new Bit[]{ONE, ZERO, ZERO, ONE, ONE, ZERO, ONE, ZERO};
//
//        //Bit[] bits = doHalfmanAlgo();
//
//        CompressionResultBuilder newBuilder = newBuilder();
//        for (Bit bit : bits) {
//            newBuilder.addBit(bit);
//        }
//        newBuilder.setFileName("java");
//        CompressionResult result = newBuilder.build();
//
//        System.out.println(result.getBites());

        Node node = new Node();
        node.setValue(45);
        node.setValue(45);

        PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingInt(Node :: getWeight));

    }

}
