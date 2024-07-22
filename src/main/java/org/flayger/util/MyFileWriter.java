package org.flayger.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class MyFileWriter {

    BufferedWriter integerWriter;
    BufferedWriter floatWriter;
    BufferedWriter stringWriter;

    String outputPath;

    String outputPrefix;

    boolean isAppend;

    private static String stringFileName = "strings.txt";
    private static String integerFileName = "integers.txt";
    private static String floatFileName = "floats.txt";

    public MyFileWriter(String outputPath, String outputPrefix, boolean isAppend) {
        this.outputPath = outputPath;
        this.outputPrefix = outputPrefix;
        this.isAppend = isAppend;
    }

    public void write(BigInteger input) throws IOException {
        File integerContentFile = new File(outputPath + '/' + outputPrefix + integerFileName);
        if (!integerContentFile.exists()) {
            integerContentFile.getParentFile().mkdirs(); // Will create parent directories if not exists
        }
        if (integerWriter == null) {
            this.integerWriter = new BufferedWriter(new FileWriter(integerContentFile, isAppend));
        }
        integerWriter.write(String.valueOf(input));
        integerWriter.newLine();
    }

    public void write(BigDecimal input) throws IOException {
        File floatContentFile = new File(outputPath + '/' + outputPrefix + floatFileName);
        if (!floatContentFile.exists()) {
            floatContentFile.getParentFile().mkdirs(); // Will create parent directories if not exists
        }
        if (floatWriter == null) {
            this.floatWriter = new BufferedWriter(new FileWriter(floatContentFile, isAppend));
        }
        floatWriter.write(String.valueOf(input));
        floatWriter.newLine();
    }

    public void write(String input) throws IOException {
        File stringContentFile = new File(outputPath + '/' + outputPrefix + stringFileName);
        if (!stringContentFile.exists()) {
            stringContentFile.getParentFile().mkdirs(); // Will create parent directories if not exists
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
