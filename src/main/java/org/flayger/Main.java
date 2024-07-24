package org.flayger;

import org.apache.commons.cli.*;
import org.flayger.statistics.ShortStatistics;
import org.flayger.statistics.Statistics;
import org.flayger.util.ExtendedParser;
import org.flayger.util.MyExecutorWriter;
import org.flayger.statistics.FullStatistics;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static List<String> fileNames;
    private static MyExecutorWriter myExecutorWriter;
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
        try {
            parseArguments(args);
        } catch (ParseException e) {
            System.err.println("Ошибка во время обработки команд командной строки: " + e.getMessage());
        }
        /*
        какие еще ошибки может выбросить parser?
        перечислены эти, но они не могут выскочить. right?
        UnsupportedOperationException – if the add operation is not supported by this list
ClassCastException – if the class of the specified element prevents it from being added to this list
NullPointerException – if the specified element is null and this list does not permit null elements
IllegalArgumentException
         */

        /*
        открыть файлы и начать их читать, перезаписывая/добавляя данные в файлы вывода
        */
        processFiles();
        //когда закрывать? writer в файлы может runtime выкинуть?
        myExecutorWriter.close();

        /*
        вывести статистику, если на ее сбор есть указание
         */
        if (myStatistics != null) {
            myStatistics.show();
        }
    }

    private static void processFiles() {
        /*
        пройти по файлам, обновить статистику
        вынести создание файла и запись в него отдельный класс
         */
        if (fileNames.isEmpty()) {
            throw new RuntimeException("не указано ни одного файла");
        }

        for (String name : fileNames) {
            //надо обработать ошибку, если файл не найден
            /*
            надо все таки в отдельный класс - хранить 3 записывателя в эти файлы.
            методы записи будут обращаться к ним, в конце работы программы - закрыть записыватели
             */
            try {
                Scanner scanner = getScanner(name);
                while (scanner.hasNext()) {
                    if (scanner.hasNextBigInteger()) {
                        BigInteger input = scanner.nextBigInteger();
                        if (myStatistics != null) myStatistics.update(input);
                        myExecutorWriter.write(input);
                    } else if (scanner.hasNextBigDecimal()) {
                        BigDecimal input = scanner.nextBigDecimal();
                        if (myStatistics != null) myStatistics.update(input);
                        myExecutorWriter.write(input);
                    } else {
                        String input = scanner.nextLine();
                        if (input.isEmpty())
                            continue;
                        if (myStatistics != null) myStatistics.update(input);
                        myExecutorWriter.write(input);
                    }
                }
            } catch (SecurityException e) {
                //что за ошибка, когда возникает? createDirectories
                System.err.println("ошибка доступа к директории " + e.getMessage());
            } catch (InvalidPathException e) {
                System.err.println("неправильно указан путь до файла " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.err.println("не найден файл " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.err.println("ошибка при чтении строки из файла " + e.getMessage());
            } catch (IOException e) {
                System.err.println("ошибка при записи в файл " + e.getMessage());
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
        if (args.length == 0) {
            throw new RuntimeException("не указано ни одного параметра командной строки");
        }

        /*
        мне не нравится, что он ловит любые "опции" o, p , a , s, f без черточек = файлы с этими именами не обработает
         */
        //основные опции аргументов командной строки
        Options options = new Options();
        options.addOption("o", true, "задает путь до результатов");
        options.addOption("p", true, "задает префикс имен выходных файлов");
        options.addOption("a", false, "установить режим добавления в существующие файлы");
        options.addOption("s", false, "установить сбор краткой статистики");
        options.addOption("f", false, "установить сбор полной статистики");


        //синтаксический анализ аргументов командной строки
        ExtendedParser commandLineParser = new ExtendedParser();
        CommandLine cmd = commandLineParser.parse(options, args);

        //обработка пришедших аргументов командной строки
        /*
        как именно они хотят указывать путь до файла - это относительный? /some/folder?
        или абсолютный - от корня каталога? и тогда относительный был бы some/folder?
         */

        Path outputPath = Path.of(cmd.getOptionValue("o"));
        String outputPrefix = cmd.getParsedOptionValue("p", "");

        boolean isAppend = cmd.hasOption("a");

//        path.resolve(outputPath).resolve(outputPrefix);
//        System.out.println();
        if (cmd.hasOption("s"))
            myStatistics = new ShortStatistics();
        if (cmd.hasOption("f"))
            myStatistics = new FullStatistics();

        myExecutorWriter = new MyExecutorWriter(outputPath, outputPrefix, isAppend);

//
//        MyWriter integerWriter = new MyWriter(outputPath.resolve(outputPrefix + "integers.txt").toFile(), isAppend);
//        MyWriter floatWriter = new MyWriter(outputPath.resolve(outputPrefix + "floats.txt").toFile(), isAppend);
//        MyWriter stringWriter = new MyWriter(outputPath.resolve(outputPrefix + "strings.txt").toFile(), isAppend);


        //все, что не попало в опции - считается файлом
        fileNames = commandLineParser.getNotParsedArgs();
    }
}