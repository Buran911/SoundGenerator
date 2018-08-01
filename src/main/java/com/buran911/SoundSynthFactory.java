package com.buran911;

import com.buran911.utils.AutoCloser;
import lombok.AllArgsConstructor;

import java.nio.file.Path;
import java.sql.*;
import java.util.*;

import static java.lang.String.format;

/**
 * Фабрика синтезаторов.
 */
@AllArgsConstructor
public class SoundSynthFactory {
    // Шаблон для запроса к бд на получение частот нот заданной октавы
    private static final String FREQ_QUERY_TEMPLATE = "SELECT tone, frequency_val " +
            "FROM frequency JOIN octave USING(octave_id) " +
            "WHERE octave.octave_name=\"%s\"";

    // Путь к базе данных с частотами нот
    private Path dbPath;

    /**
     * Создает синтезатор по названию октавы.
     */
    public SoundSynth createSoundSynth(String octaveName) {
        return new SoundSynth(octaveName, loadFrequences(octaveName, dbPath));
    }

    /**
     * Загружает из БД частоты нот для заданной октавы.
     */
    private Map<String, Double> loadFrequences(String octaveName, Path dbPath) {
        Map<String, Double> noteFrequencies = new HashMap<>();
        try (AutoCloser dbCloser = new AutoCloser()) {

            Class.forName("org.sqlite.JDBC");
            Connection connection = dbCloser.register(DriverManager.getConnection("jdbc:sqlite:" + dbPath));
            Statement st = dbCloser.register(connection.createStatement());

            String query = format(FREQ_QUERY_TEMPLATE, octaveName);
            ResultSet rs = dbCloser.register(st.executeQuery(query));
            while (rs.next()) {
                noteFrequencies.put(rs.getString("tone"), rs.getDouble("frequency_val"));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load frequency map from DB " + dbPath, ex);
        }
        return noteFrequencies;
    }
}
