package com.alvevel;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CompressionResult {
    private final List<Integer> bites;
    private Node huffmanTree;
    private int bitLength;
    private final String fileName;

    public CompressionResult(List<Integer> bites, int resultBitLength,
                             String fileName, Node huffmanTree) {
        this.bites = bites;
        this.bitLength = resultBitLength;
        this.fileName = fileName;
        this.huffmanTree = huffmanTree;
    }

    public static CompressionResultBuilder newBuilder(){
        return new CompressionResultBuilder();
    }


    public void writeToFile() throws IOException {
        Path compressedFile = Paths.get(fileName + ".hcf");
        Path tree = Paths.get(fileName + ".ht");
        try (OutputStream out = Files.newOutputStream(compressedFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                     Files.newOutputStream(tree))){
            objectOutputStream.writeObject(huffmanTree);
            byte[] bitLengthInBytes = ByteBuffer.allocate(4).putInt(bitLength).array();
            out.write(bitLengthInBytes);
            for (int oneByte : bites) {
                out.write(oneByte);
            }
        }
    }
}
