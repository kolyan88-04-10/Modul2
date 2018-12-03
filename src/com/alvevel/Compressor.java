package com.alvevel;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Compressor {

    private Compressor() {
    }

    public static void main(String[] args) {
        compress("test.txt", "test");
    }

    public static void compress(String sourceFileName, String compressedFileName) {
        Path sourceFile = Paths.get(sourceFileName);
        if (!Files.exists(sourceFile)) {
            System.out.println("Source file is not exists");
        } else if (!Files.isRegularFile(sourceFile)) {
            System.out.println("Source file is not a regular file");
        } else {
            try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(sourceFile))){
                in.mark(Integer.MAX_VALUE);
                int[] frequency = buildFrequency(in);
                Node tree = buildTree(frequency);
                Map<Integer, String> huffmanTable = buildTable(tree);
                in.reset();
                String stringPresentationCompression =
                        buildStringPresentationCompression(in, huffmanTable);
                CompressionResultBuilder builder = new CompressionResultBuilder();
                builder.setFileName(compressedFileName);
                builder.setHuffmanTree(tree);
                for (int i = 0; i < stringPresentationCompression.length(); i++) {
                    builder.addBit(Bit.getBit(stringPresentationCompression.charAt(i)));
                }
                Result compressionResult = builder.build();
                compressionResult.writeToFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to build string representation of bits in compressed file
     * @param stream
     * @param huffmanTable
     * @return
     * @throws IOException
     */
    private static String buildStringPresentationCompression(
            BufferedInputStream stream, Map<Integer, String> huffmanTable) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (stream.available() > 0) {
            String codeForByte = huffmanTable.get(stream.read());
            builder.append(codeForByte);
        }
        return builder.toString();
    }

    private static Map<Integer, String> buildTable(Node tree) {
        Map<Integer, String> huffmanTable = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        getEntry(tree, huffmanTable, stringBuilder);
        return huffmanTable;
    }

    /**
     * Method to get entries to write in huffman table
     * @param node
     * @param huffmanTable
     * @param stringBuilder
     */
    private static void getEntry(Node node, Map<Integer, String> huffmanTable,
                                 StringBuilder stringBuilder) {
        Node left = node.getLeft();
        Node right = node.getRight();
        if (left == null && right == null) {
            huffmanTable.put(node.getValue(), stringBuilder.toString());
            stringBuilder.setLength(stringBuilder.length() - 1);
        } else {
            if (left != null){
                stringBuilder.append(0);
                getEntry(left, huffmanTable, stringBuilder);
            }
            if (right != null) {
                stringBuilder.append(1);
                getEntry(right, huffmanTable, stringBuilder);
            }
            stringBuilder.setLength(stringBuilder.length() > 1
                    ? stringBuilder.length() - 1 : 0);
        }
    }

    private static Node buildTree(int[] frequencyList) {
        PriorityQueue<Node> queue  = new PriorityQueue<>(
                Comparator.comparingInt(Node :: getWeight));
        int frequency;
        for (int value  = 0; value < frequencyList.length; value++) {
            frequency = frequencyList[value];
            if (frequency != 0) {
                Node node = new Node(value, frequency);
                queue.offer(node);
            }
        }
        Node node;

        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            int parentNodeWeight = left.getWeight() + right.getWeight();
            node = new Node(parentNodeWeight);
            node.setLeft(left);
            node.setRight(right);
            queue.offer(node);
        }

        node = queue.poll();
        return node;
    }

    private static int[] buildFrequency (InputStream inputStream) throws IOException {
        int[] frequency = new int[256];
        while (inputStream.available() > 0) {
            int oneByte = inputStream.read();
            frequency[oneByte]++;
        }
        return frequency;
    }
}
