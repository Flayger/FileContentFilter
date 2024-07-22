package org.flayger.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class MyFileWriter {

    private BufferedWriter integerWriter;
    private BufferedWriter floatWriter;
    private BufferedWriter stringWriter;
    private final String outputPath;
    private final String outputPrefix;
    private final boolean isAppend;

    public MyFileWriter(String outputPath, String outputPrefix, boolean isAppend) {
        this.outputPath = outputPath;
        this.outputPrefix = outputPrefix;
        this.isAppend = isAppend;
    }

    public void write(BigInteger input) throws IOException {
        String integerFileName = "integers.txt";
        File integerContentFile = new File(outputPath + '/' + outputPrefix + integerFileName);
        if (!integerContentFile.exists()) {
            boolean isCreated = integerContentFile.getParentFile().mkdirs();
            if (!isCreated) {
                throw new IOException("ошибка при создании директории для файла вывода для целых");
            }

        }
        if (integerWriter == null) {
            this.integerWriter = new BufferedWriter(new FileWriter(integerContentFile, isAppend));
        }
        integerWriter.write(String.valueOf(input));
        integerWriter.newLine();
    }

    public void write(BigDecimal input) throws IOException {
        String floatFileName = "floats.txt";
        File floatContentFile = new File(outputPath + '/' + outputPrefix + floatFileName);
        if (!floatContentFile.exists()) {
            boolean isCreated = floatContentFile.getParentFile().mkdirs();
            if (!isCreated) {
                throw new IOException("ошибка при создании директории для файла вывода для вещественных");
            }
        }
        if (floatWriter == null) {
            this.floatWriter = new BufferedWriter(new FileWriter(floatContentFile, isAppend));
        }
        floatWriter.write(String.valueOf(input));
        floatWriter.newLine();
    }

    public void write(String input) throws IOException {
        String stringFileName = "strings.txt";
        File stringContentFile = new File(outputPath + '/' + outputPrefix + stringFileName);
        if (!stringContentFile.exists()) {
            boolean isCreated = stringContentFile.getParentFile().mkdirs();
            if (!isCreated) {
                throw new IOException("ошибка при создании директории для файла вывода для строк");
            }
        }
        if (stringWriter == null) {
            this.stringWriter = new BufferedWriter(new FileWriter(stringContentFile, isAppend));
        }
        stringWriter.write(String.valueOf(input));
        stringWriter.newLine();
    }


    public void close() throws IOException {

        if (integerWriter != null) integerWriter.close();
        if (floatWriter != null) floatWriter.close();
        if (stringWriter != null) stringWriter.close();
    }
}
