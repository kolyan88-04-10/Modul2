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
        decompress("compressedFile.hcf", "compressedFile.ht", "result.txt");
    }

    public static void decompress(String compressedFileName, String fileTreeName, String destinationFileName) {
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
                Node tree = getTree(fileTreeName);
                String stringPresentationCompression = buildStringPresentationCompression(in, bitLength);
                DecopressionResultBuilder builder = new DecopressionResultBuilder(tree, destinationFileName);
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

    private static Node getTree(String fileTreeName) {
        return null;
    }

    private static int getBitLength(BufferedInputStream in) throws IOException {
        byte[] buff = new byte[4];
        for (int i = 0; i < 4; i++) {
            buff[i] = (byte) in.read();
        }
        ByteBuffer bb = ByteBuffer.wrap(buff);
        return bb.getInt();
    }

    private static String buildStringPresentationCompression(BufferedInputStream in, int bitLength) throws IOException {
        StringBuilder sb = new StringBuilder();
        int position = 0;
        while (in.available() > 0) {
            int oneByte = in.read();

        }
        return null;
    }
}
