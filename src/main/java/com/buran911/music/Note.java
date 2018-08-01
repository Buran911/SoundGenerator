package com.buran911.music;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс - нота.
 */
@AllArgsConstructor
@Getter
public class Note {
    private String tone;        //  Тональность
    private String duration;    //  Длительность
}
