package com.buran911;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

/**
 * Основной класс приложения для проигрывания xml-файлов с нотами.
 */

public class Application {

    private static final Logger LOG = Logger.getLogger(Application.class);

    /**
     * Проигрывает трек из xml-файла.
     * В качестве параметра передается путь к xml-файлу с треком.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new RuntimeException("Application needs one parameter: path/to/NoteListFile");
        }

        Path noteListPath = Paths.get(args[0]);
        if (!Files.exists(noteListPath) || Files.isDirectory(noteListPath) || !Files.isReadable(noteListPath)) {
            throw new RuntimeException("NoteListFile should exist and be a readable file");
        }

        try {
            LOG.info(format("Starting to play file %s ...", args[0]));
            createXmlPlayer().playXmlFile(noteListPath);
            LOG.info("Done");
        } catch (Exception ex) {
            LOG.error(format("Failed to play file %s", args[0]));
            ex.printStackTrace();
            throw new RuntimeException(format("Unable to playXmlFile file %s\r\nCause: %s", noteListPath, ex.getMessage()));
        }
    }

    /**
     * Создает плеер для xml-файлов.
     */
    private static XmlMusicPlayer createXmlPlayer() {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
            return (XmlMusicPlayer) context.getBean("xmlPlayer");
        } catch (Exception ex) {
            throw new RuntimeException("Unable to create XmlPlayer");
        }
    }
}