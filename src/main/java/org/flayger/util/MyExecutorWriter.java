package org.flayger.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;

public class MyExecutorWriter {
    private final MyFileWriter integerWriter;
    private final MyFileWriter floatWriter;
    private final MyFileWriter stringWriter;

    public MyExecutorWriter(Path outputPath, String outputPrefix, boolean isAppend) {
        File integerFile = outputPath.resolve(outputPrefix + "integers.txt").toFile();
        File floatFile = outputPath.resolve(outputPrefix + "floats.txt").toFile();
        File stringFile = outputPath.resolve(outputPrefix + "strings.txt").toFile();

        this.integerWriter = new MyFileWriter(integerFile, isAppend);
        this.floatWriter = new MyFileWriter(floatFile, isAppend);
        this.stringWriter = new MyFileWriter(stringFile, isAppend);
    }

    /*
    я могу вынести File - int, float, string
    и вынести writer
    в отдельный метод - чтобы вызывать на нем проверку директорий и запись в этот файл
     */


    /*
    как сделать красиво -
    сейчас я считываю разные типы данных для сбора статистики - статистику можно хранить в writer-е определенного типа?
    из-за этого у меня можно перегрузить метод для записи по типу данных - инты, вещественные, строки
    но я все их превращаю в string при записи - если я не буду пользоваться перегрузкой и сразу передавать String, то мне нужно
    второй параметр - тип записываемых данных, но тогда в методе будет условие по этому типу, но метод будет один.

    есть еще вариант вынести в Main логику типов обрабатываемых данных и просто вызывать write у нужного типа
     */
    public void write(BigInteger input) throws IOException {
        integerWriter.write(String.valueOf(input));
    }

    public void write(BigDecimal input) throws IOException {
        floatWriter.write(String.valueOf(input));
    }

    public void write(String input) throws IOException {
        stringWriter.write(input);
    }

    public void close() {
        if (integerWriter != null) integerWriter.close();
        if (floatWriter != null) floatWriter.close();
        if (stringWriter != null) stringWriter.close();

    }
}
