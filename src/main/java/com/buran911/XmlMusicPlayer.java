package com.buran911;

import com.buran911.music.Composition;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

import javax.sound.sampled.AudioFormat;
import java.nio.file.Path;
import java.util.List;

/**
 * Плеер для проигрывания композиций.
 */
@AllArgsConstructor
public class XmlMusicPlayer implements InitializingBean {
    // Формат воспроизведения.
    private AudioFormat audioFormat;
    // Синтезаторы для нот.
    private List<SoundSynth> synths;

    public void afterPropertiesSet(){
        synths.forEach(synth -> synth.setAudioFormat(audioFormat));
    }

    /**
     * Проигрывает трек из xml-файла.
     */
    public void playXmlFile(Path compositionFile) {
        Composition composition = XmlCompositionReader.readComposition(compositionFile);
        playComposition(composition);
    }

    /**
     * Проигрывает композицию с помощью всех имеющихся синтезаторов.
     */
    private void playComposition(Composition composition) {
        synths.forEach(soundSynth -> soundSynth.playComposition(composition));
    }
}
