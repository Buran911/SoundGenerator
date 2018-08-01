package com.buran911;

import com.buran911.music.Composition;
import com.buran911.music.Note;
import com.buran911.utils.DurationConvertor;
import lombok.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Синтезатор звука для заданной октавы.
 */
public class SoundSynth {
    // Название используемой октавы.
    private String octaveName;
    // Карта частот нот.
    private Map<String, Double> tonesFreqMap;
    // Формат воспроизведения
    @Setter
    private AudioFormat audioFormat;

    public SoundSynth(String octaveName, Map<String, Double> tonesFreqMap) {
        this.octaveName = octaveName;
        this.tonesFreqMap = tonesFreqMap;
    }

    /**
     * Проигрывает заданную композицию.
     */
    public void playComposition(Composition composition) {
        int temp = composition.getTemp();
        playSoundWaves(
                composition.getNotes().stream()
                        .map(note -> createSoundWave(note, temp))
                        .collect(Collectors.toList())
        );
    }

    /**
     * Преобразует данные ноты в звуковую волну.
     */
    private SoundWave createSoundWave(Note note, int temp) {

        // Длительность ноты в миллисекундах
        double duration = 60_000 / temp * DurationConvertor.duration2Double(note.getDuration());

        // Размер буфера под сигнал
        byte[] buffer = new byte[(int) (duration * audioFormat.getSampleRate() / 1000)];

        Double frequency = tonesFreqMap.get(note.getTone());
        if (frequency != null) {

            // Строим синусоиду
            double period = audioFormat.getSampleRate() / tonesFreqMap.get(note.getTone());
            for (int i = 0; i < buffer.length; i++) {
                double angle = 2d * Math.PI * i / period;
                buffer[i] = (byte) (Math.sin(angle) * 127d);
            }
        } else {
            Arrays.fill(buffer, (byte) 0);
        }
        return new SoundWave(buffer);
    }

    /**
     * Воспроизводит звуковые волны из спика.
     */
    private void playSoundWaves(List<SoundWave> soundWaves) {
        try (SourceDataLine line = AudioSystem.getSourceDataLine(audioFormat)) {
            line.open(audioFormat);
            line.start();

            soundWaves.forEach(soundWave -> {
                byte[] buffer = soundWave.getData();
                line.write(buffer, 0, buffer.length);
            });

            line.drain();
        } catch (Exception ex) {
            throw new RuntimeException(format("%s failed to play sound waves", getClass().toString()), ex);
        }
    }

    /**
     * Звуковая волна для проигрывания, соответствущая одной ноте.
     */
    @AllArgsConstructor
    @Getter
    private static class SoundWave {
        byte[] data;    // данные
    }
}
