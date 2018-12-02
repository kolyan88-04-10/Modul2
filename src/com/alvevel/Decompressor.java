package com.alvevel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Map;

public class Decompressor {
    public static void main(String[] args) {
        decompress("compressedFile.hcf", "compressedFile.ht");
    }

    public static void decompress(String compressedFileName, String fileTableName) {
        Path compressedFile = Paths.get(compressedFileName);
        Path fileTable = Paths.get(compressedFileName);
        if (!Files.exists(compressedFile)) {
            System.out.println("Compressed file is not exists");
        } else if (!Files.isRegularFile(compressedFile)) {
            System.out.println("Compressed file is not a regular file");
        } else if (!Files.exists(fileTable)) {
            System.out.println("FileTable file is not exists");
        } else if (!Files.isRegularFile(fileTable)) {
            System.out.println("FileTable file is not a regular file");
        } else {
            try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(compressedFile))){
                int bitLength = getBitLength(in);
                Map<Integer, String> haffmanTale = getTable(fileTable);
                String stringPresentationCompression = buildStringPresentationCompression(compressedFile);
                DecopressionResultBuilder builder = new DecopressionResultBuilder();
                for (int i = 0; i < stringPresentationCompression.length(); i++) {
                    builder.addBit(Bit.getBit(stringPresentationCompression.charAt(i)));
                }
                DecompressionResult decompressionResult = builder.build();
                decompressionResult.writeToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static int getBitLength(BufferedInputStream in) throws IOException {
        byte[] buff = new byte[4];
        for (int i = 0; i < 4; i++) {
            buff[i] = (byte) in.read();
        }
        ByteBuffer bb = ByteBuffer.wrap(buff);
        return bb.getInt();
    }

    private static String buildStringPresentationCompression(Path compressedFile) {
        
        return null;
    }

    private static Map<Integer, String> getTable(Path fileTable) {
        return null;
    }
}
