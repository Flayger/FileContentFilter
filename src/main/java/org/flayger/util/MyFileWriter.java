package org.flayger.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;

public class MyFileWriter {
    private BufferedWriter writer;
    private final File file;
    private final boolean isAppend;

    public MyFileWriter(File file, boolean isAppend) {
        this.file = file;
        this.isAppend = isAppend;
    }

    public void write(String input) throws IOException, InvalidPathException, SecurityException {
        createDirectory(file);
        if (writer == null) {
            this.writer = new BufferedWriter(new FileWriter(file, isAppend));
        }
        writer.write(input);
        writer.newLine();
    }

    private void createDirectory(File contentFile) throws IOException, InvalidPathException, SecurityException {
        if (!contentFile.exists()) {
            Files.createDirectories(contentFile.getParentFile().toPath());
            //contentFile.getParentFile().mkdirs();
        }
    }

    public void close() {
        try {
            this.writer.close();
        } catch (IOException e) {
            System.err.println("ошибка при закрытии писателя в файл " + file + e.getMessage());
        }
    }
}
