package com.alvevel;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Compressor {
    private Node tree;
    private String sourceFileName;
    private String compressedFileName;
    private int[] frequency = new int[256];

    public Compressor(String sourceFileName, String compressedFileName) {
        this.sourceFileName = sourceFileName;
        this.compressedFileName = compressedFileName;
    }

    public static void main(String[] args) throws IOException {
        compress("test.txt", "compressedFile");
//        String test = "abracadabra";
//        try (InputStream in = new ByteArrayInputStream(
//                test.getBytes());
//             ByteArrayOutputStream out = new ByteArrayOutputStream()){
//            int[] frequencyList = buildFrequency(in);
//            //System.out.println(Arrays.toString(frequencyList));
//            Node root = buildTree(frequencyList);
//            System.out.println(root);
//
//            Map<Integer, String> huffmanTable = buildTable(root);
//            for (Map.Entry<Integer, String> entry : huffmanTable.entrySet()) {
//                System.out.print((char)entry.getKey().intValue() + "/" + entry.getValue() + ", ");
//            }
//            System.out.println(huffmanTable);
//
//            in.reset();
//            String stringPresentationCompression =
//                    buildStringPresentationCompression(in, huffmanTable);
//            System.out.println(stringPresentationCompression);
//
//            List<Integer> compressionBytes = getCompressionBytes(stringPresentationCompression);
//            for (Integer compressionByte : compressionBytes) {
//                System.out.print(Integer.toBinaryString(compressionByte) + ", ");
//            }
//            writeCompression(compressionBytes, stringPresentationCompression.length(),
//                    out);
//            System.out.println();
//            byte[] buff = new byte[4];
//            byte[] byteArray = out.toByteArray();
//            for (int i = 0; i < 4; i ++) {
//                buff[i] = byteArray[i];
//            }
//            ByteBuffer bb = ByteBuffer.wrap(buff);
//            System.out.println(bb.getInt());
//            for (int i = 4; i < byteArray.length; i++) {
//                System.out.print((byteArray[i] & 0xFF) + ", ");
//            }
//        }
    }

    public static void compress(String sourceFileName, String compressedFileName) {
        Path sourceFile = Paths.get(sourceFileName);
        Path compressedFile = Paths.get(compressedFileName);
        if (!Files.exists(sourceFile)) {
            System.out.println("Source file is not exists");
        } else if (!Files.isRegularFile(sourceFile)) {
            System.out.println("Source file is not a regular file");
        } else {
            try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(sourceFile))){
                in.mark(0);
                int[] frequency = buildFrequency(in);
                Node tree = buildTree(frequency);
                Map<Integer, String> huffmanTable = buildTable(tree);
                in.reset();
                String stringPresentationCompression =
                        buildStringPresentationCompression(in, huffmanTable);
                CompressionResultBuilder builder = new CompressionResultBuilder();
                builder.setFileName(compressedFileName);
                builder.setHuffmanTable(huffmanTable);
                for (int i = 0; i < stringPresentationCompression.length(); i++) {
                    builder.addBit(Bit.getBit(stringPresentationCompression.charAt(i)));
                }
                CompressionResult compressionResult = builder.build();
                compressionResult.writeToFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Integer> getCompressionBytes(String stringPresentationCompression) {
        List<Integer> compressionBytes = new ArrayList<>();
        int oneByte = 0;
        int position = 0;
        Bit bit = Bit.getBit(stringPresentationCompression.charAt(position));
        oneByte |= bit.value();
        oneByte <<= 1;
        for (position = 1; position < stringPresentationCompression.length(); position++) {
            bit = Bit.getBit(stringPresentationCompression.charAt(position));
            oneByte |= bit.value();
            if ((position + 1) % 8 == 0) {
                compressionBytes.add(oneByte);
                oneByte = 0;
            } else {
                oneByte <<= 1;
            }
        }
        /*
        number shows how many times we need shift bits to left if result
        not contains whole bites;
         */
        int lastByteShift = 8 - stringPresentationCompression.length() % 8;
        if (lastByteShift != 8) {
            oneByte <<= lastByteShift;
        }
        compressionBytes.add(oneByte);
        return compressionBytes;
    }

    private static String buildStringPresentationCompression(
            BufferedInputStream stream, Map<Integer, String> huffmanTable) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (stream.available() > 0) {
            String codeForByte = huffmanTable.get(stream.read());
            builder.append(codeForByte);
        }
        return builder.toString();
    }

    public static void buildResult(String fileName) {
        CompressionResultBuilder builder = CompressionResult.newBuilder();
        builder.setFileName(fileName);
    }

    private static Map<Integer, String> buildTable(Node tree) {
        Map<Integer, String> huffmanTable = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        Node left = tree.getLeft();
        stringBuilder.append(0);
        getEntry(left, huffmanTable, stringBuilder);
        stringBuilder.setLength(0);
        stringBuilder.append(1);
        Node right = tree.getRight();
        getEntry(right, huffmanTable, stringBuilder);
        return huffmanTable;
    }

    private static void getEntry(Node node, Map<Integer, String> huffmanTable,
                                 StringBuilder stringBuilder) {
        Node left = node.getLeft();
        Node right = node.getRight();
        if (left == null && right == null) {
            huffmanTable.put(node.getValue(), stringBuilder.toString());
            stringBuilder.setLength(stringBuilder.length() - 1);
        } else {
            stringBuilder.append(0);
            getEntry(left, huffmanTable, stringBuilder);
            stringBuilder.append(1);
            getEntry(right, huffmanTable, stringBuilder);
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

    private static void writeCompression(List<Integer> compressionBytes,
                                         int bitLength, OutputStream out) throws IOException {
        byte[] bitLengthInBytes = ByteBuffer.allocate(4).putInt(bitLength).array();
        out.write(bitLengthInBytes);
        for (Integer compressionByte : compressionBytes) {
            out.write(compressionByte);
        }
    }
}
