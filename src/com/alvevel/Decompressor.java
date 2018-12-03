package com.alvevel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Decompressor {

    private Decompressor() {}

    public static void main(String[] args) {
        decompress("readme.hcf", "readme.ht", "readmeUnpack.txt");
    }

    public static void decompress(String compressedFileName, String fileTreeName, String destinationFileName) {
        Path compressedFile = Paths.get(compressedFileName);
        Path fileTree = Paths.get(fileTreeName);
        if (!Files.exists(compressedFile)) {
            System.out.println("Compressed file is not exists");
        } else if (!Files.isRegularFile(compressedFile)) {
            System.out.println("Compressed file is not a regular file");
        } else if (!Files.exists(fileTree)) {
            System.out.println("FileTable file is not exists");
        } else if (!Files.isRegularFile(fileTree)) {
            System.out.println("FileTable file is not a regular file");
        } else {
            try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(compressedFile));
                 ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(fileTree))){
                int bitLength = getBitLength(in);
                Node tree = (Node) objectInputStream.readObject();
                String stringPresentationCompression = buildStringPresentationCompression(in, bitLength);
                DecopressionResultBuilder builder = new DecopressionResultBuilder(tree, destinationFileName);
                for (int i = 0; i < stringPresentationCompression.length(); i++) {
                    builder.addBit(Bit.getBit(stringPresentationCompression.charAt(i)));
                }
                DecompressionResult decompressionResult = builder.build();
                decompressionResult.writeToFile();
            } catch (IOException | ClassNotFoundException e) {
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

    private static String buildStringPresentationCompression(BufferedInputStream in, int bitLength) throws IOException {
        StringBuilder sb = new StringBuilder();
        int wholeByteNumber = bitLength / 8;
        int oneByte;
        for (int i = 0; i < wholeByteNumber; i++) {
            oneByte = in.read() & 0xFF;
            writeBits(oneByte, sb);
        }
        int remainder = bitLength % 8;
        if (remainder != 0) {
            oneByte = in.read() & 0xFF;
            oneByte >>= 8 - remainder;
            for (int i = 7; i >= 8 - remainder; i--) {
                sb.append(getBit(oneByte, i));
            }
        }
        return sb.toString();
    }

    /**
     * writes bit representation of number to StingBuilder
     * @param oneByte
     * @param sb
     */
    private static void writeBits(int oneByte, StringBuilder sb) {
        for (int i = 7; i >= 0; i-- ) {
            sb.append(getBit(oneByte, i));
        }
    }

    /**
     * returns bit in special position in int number
     * @param n
     * @param k
     * @return
     */
    private static int getBit(int n, int k) {
        return (n >> k) & 1;
    }
}
