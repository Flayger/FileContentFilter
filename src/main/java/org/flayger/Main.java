package org.flayger;

import org.apache.commons.cli.*;
import org.flayger.statistics.ShortStatistics;
import org.flayger.statistics.Statistics;
import org.flayger.util.ExtendedParser;
import org.flayger.util.MyFileWriter;
import org.flayger.statistics.FullStatistics;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static List<String> fileNames;
    private static MyFileWriter myFileWriter;
    private static Statistics myStatistics;

    public static void main(String[] args) {
        //запарсить аргументы командной строки
        /*
        проверить -
        путь до файлов вывода
        префиксы для файлов вывода
        опцию для перезаписи
        опцию для сбора короткой или полной статистики

        сделать отдельный класс для сбора статистики?

        проверить - parseException - когда вылетает, какие команды успевает считать

        написал отдельный парсер, который сохраняет список параметров, которые не учитывались
         */
//        parseArguments(args);
        try {
            parseArguments(args);
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
        }

        /*
        открыть файлы и начать их читать, перезаписывая/добавляя данные в файлы вывода
        */

        try {
            processFiles();
        } catch (NoSuchElementException e) {
            System.out.println("ошибка при чтении строки из файла " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("не найден файл " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ошибка при записи в файл " + e.getMessage());
        }
        try {
            myFileWriter.close();
        } catch (IOException e) {
            System.out.println("ошибка при закрытии записи в файл " + e.getMessage());
        }

        /*
        вывести статистику, если на ее сбор есть указание
         */
        if (myStatistics != null) {
            myStatistics.show();
        }


    }

    private static void processFiles() throws IOException {
        /*
        пройти по файлам, обновить статистику

        вынести создание файла и запись в него отдельный класс
         */

        for (String name : fileNames) {
            //надо обработать ошибку, если файл не найден
            /*
            надо все таки в отдельный класс - хранить 3 записывателя в эти файлы.
            методы записи будут обращаться к ним, в конце работы программы - закрыть записыватели
             */

            Scanner scanner = getScanner(name);
            while (scanner.hasNext()) {
                if (scanner.hasNextBigInteger()) {
                    BigInteger input = scanner.nextBigInteger();
                    //обновить статистику?
                    myStatistics.update(input);
                    myFileWriter.write(input);
                } else if (scanner.hasNextBigDecimal()) {
                    BigDecimal input = scanner.nextBigDecimal();
                    //обновить статистику?
                    myStatistics.update(input);
                    myFileWriter.write(input);
                } else {
                    String input = scanner.nextLine();
                    if (input.isEmpty())
                        continue;
                    //обновить статистику?
                    myStatistics.update(input);
                    myFileWriter.write(input);
                }
            }
        }

    }

    private static Scanner getScanner(String name) throws FileNotFoundException {
        File file = new File(name);
        Scanner scanner = new Scanner(file);

        //без локали неправильно определяет , и . в вещественных числах
        scanner.useLocale(Locale.ENGLISH);
        return scanner;
    }

    private static void parseArguments(String[] args) throws ParseException {
        //основные опции аргументов командной строки
        Options options = new Options();
        options.addOption("o", true, "задает путь до результатов");
        options.addOption("p", true, "задает префикс имен выходных файлов");
        options.addOption("a", false, "установить режим добавления в существующие файлы");
        options.addOption("s", false, "установить сбор краткой статистики");
        options.addOption("f", false, "установить сбор полной статистики");


        //синтаксический анализ аргументов командной строки
        ExtendedParser commandLineParser = new ExtendedParser();
        CommandLine cmd;
        cmd = commandLineParser.parse(options, args);

        //обработка пришедших аргументов командной строки
        String outputPath = cmd.getParsedOptionValue("o", "");
        String outputPrefix = cmd.getParsedOptionValue("p", "");

        boolean isAppend = cmd.hasOption("a");

        if (cmd.hasOption("s"))
            myStatistics = new ShortStatistics();
        if (cmd.hasOption("f"))
            myStatistics = new FullStatistics();

        myFileWriter = new MyFileWriter(outputPath, outputPrefix, isAppend);

        //все, что не попало в опции - считается файлом
        fileNames = commandLineParser.getNotParsedArgs();
    }
}