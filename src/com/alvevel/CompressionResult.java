package com.alvevel;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class CompressionResult {
    private CompressionResultBuilder builder;
    private final List<Integer> bites;
    private Map<Integer, String> huffmanTable;
    private int bitLength;
    private final String fileName;

    public List<Integer> getBites() {
        return bites;
    }

    public String getFileName() {
        return fileName;
    }

    public CompressionResult(List<Integer> bites, int resultBitLength,
                             String fileName, Map<Integer, String> huffmanTable) {
        this.bites = bites;
        this.bitLength = resultBitLength;
        this.fileName = fileName;
        this.huffmanTable = huffmanTable;
    }

    public static CompressionResultBuilder newBuilder(){
        return new CompressionResultBuilder();
    }


    public void writeToFile() throws IOException {
        Path compressedFile = Paths.get(fileName + ".hcf");
        Path table = Paths.get(fileName + ".ht");
        try (OutputStream out = Files.newOutputStream(compressedFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                     Files.newOutputStream(table))){
            objectOutputStream.writeObject(huffmanTable);
            byte[] bitLengthInBytes = ByteBuffer.allocate(4).putInt(bitLength).array();
            out.write(bitLengthInBytes);
            for (int oneByte : bites) {
                out.write(oneByte);
            }
        }
    }
}
