package com.buran911.music;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Музыкальная композиция.
 */
@Setter
@Getter
public class Composition {
    private String name;        // Название
    private String composer;    // Композитор
    private int temp;           // Темп
    private List<Note> notes;   // Ноты
}
