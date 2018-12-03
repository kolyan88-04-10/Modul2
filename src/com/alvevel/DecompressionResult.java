package com.alvevel;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DecompressionResult {
    private final List<Integer> bites;
    private final String decompressedFileName;

    public DecompressionResult(List<Integer> bites, String decompressedFile) {
        this.bites = bites;
        this.decompressedFileName = decompressedFile;
    }

    public void writeToFile() throws IOException {
        Path decompressedFile = Paths.get(decompressedFileName);
        try (OutputStream out = Files.newOutputStream(decompressedFile)){
            for (int oneByte : bites) {
                out.write(oneByte);
            }
        }
    }
}
