package org.flayger.util;

import org.flayger.statistics.Statistics;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

public class Worker {
    private final Map<DataType, BufferedWriter> writers = new EnumMap<>(DataType.class);
    private final Path outputPath;
    private final String outputPrefix;
    private final boolean isAppend;
    private Statistics statistics;

    public Worker(Path outputPath, String outputPrefix, boolean isAppend) {
        this.outputPath = outputPath;
        this.outputPrefix = outputPrefix;
        this.isAppend = isAppend;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void processFiles(List<String> fileNames) {
        for (String fileName : fileNames) {
            try(Scanner scanner = getScanner(fileName)) {
                while (scanner.hasNext()) {
                    if (scanner.hasNextBigInteger()) {
                        BigInteger data = scanner.nextBigInteger();
                        if (statistics != null) statistics.update(new BigDecimal(data), DataType.INTEGER);
                        write(data.toString(), DataType.INTEGER);
                    } else if (scanner.hasNextBigDecimal()) {
                        BigDecimal data = scanner.nextBigDecimal();
                        if (statistics != null) statistics.update(data, DataType.FLOAT);
                        write(data.toString(), DataType.FLOAT);
                    } else {
                        String data = scanner.nextLine();
                        if (data.isEmpty())
                            continue;
                        if (statistics != null) statistics.update(new BigDecimal(data.length()), DataType.STRING);
                        write(data, DataType.STRING);
                    }
                }
            } catch (SecurityException e) {
                System.err.println("ошибка доступа к директории: " + e.getMessage());
            } catch (InvalidPathException e) {
                System.err.println("неправильно указан путь до файла: " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.err.println("не найден файл: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.err.println("ошибка при чтении строки из файла: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("ошибка при записи в файл: " + e.getMessage());
            }
        }
        closeWriters();
    }

    private Scanner getScanner(String name) throws FileNotFoundException {
        File file = new File(name);
        Scanner scanner = new Scanner(file);
        scanner.useLocale(Locale.ENGLISH);
        return scanner;
    }

    private void write(String data, DataType type) throws IOException {
        BufferedWriter writer = writers.get(type);

        if (writer == null) {
            File file = outputPath.resolve(outputPrefix + type.fileName()).toFile();
            createDirectoryIfNeeded(file);
            writer = new BufferedWriter(new FileWriter(file, isAppend));
            writers.put(type, writer);
        }

        writer.write(data);
        writer.newLine();
    }

    private void createDirectoryIfNeeded(File file) throws IOException, InvalidPathException, SecurityException {
        if (!file.exists()) {
            Files.createDirectories(file.getParentFile().toPath());
        }
    }

    public void closeWriters() {
        for (BufferedWriter writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("ошибка при закрытии Writer: " + writer + e.getMessage());
            }
        }
    }
}
