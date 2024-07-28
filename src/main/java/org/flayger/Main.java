package org.flayger;

import org.apache.commons.cli.*;
import org.flayger.statistics.ShortStatistics;
import org.flayger.statistics.Statistics;
import org.flayger.util.ExtendedParser;
import org.flayger.statistics.FullStatistics;
import org.flayger.util.Worker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (ParseException e) {
            System.err.println("Ошибка во время обработки команд командной строки: " + e.getMessage());
        }
    }

    private static void run(String[] args) throws ParseException {
        if (args.length == 0) {
            throw new RuntimeException("не указано ни одного параметра командной строки");
        }

        Options options = new Options();
        options.addOption("o", true, "задает путь до результатов");
        options.addOption("p", true, "задает префикс имен выходных файлов");
        options.addOption("a", false, "установить режим добавления в существующие файлы");
        options.addOption("s", false, "установить сбор краткой статистики");
        options.addOption("f", false, "установить сбор полной статистики");

        ExtendedParser commandLineParser = new ExtendedParser();
        CommandLine cmd = commandLineParser.parse(options, args);

        Path outputPath;
        if (cmd.hasOption("o")) {
            outputPath = Path.of(cmd.getOptionValue("o"));
        } else {
            outputPath = Paths.get("").toAbsolutePath();
        }
        String outputPrefix = cmd.getParsedOptionValue("p", "");
        boolean isAppend = cmd.hasOption("a");

        Statistics statistics = null;
        if (cmd.hasOption("s"))
            statistics = new ShortStatistics();
        if (cmd.hasOption("f"))
            statistics = new FullStatistics();

        List<String> fileNames = commandLineParser.getNotParsedArgs();
        if (fileNames.isEmpty()) {
            throw new RuntimeException("не указано ни одного файла");
        }

        Worker worker = new Worker(outputPath, outputPrefix, isAppend);
        worker.setStatistics(statistics);
        worker.processFiles(fileNames);

        if (statistics != null) statistics.print();
    }
}