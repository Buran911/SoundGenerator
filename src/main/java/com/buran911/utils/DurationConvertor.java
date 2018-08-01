package com.buran911.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Утилита для преобразования длительности в дробь. */
@NoArgsConstructor(access = AccessLevel.NONE)
public abstract class DurationConvertor {

    /** Конвертирует длительность в строковом виде в числовой множитель в формате double. */
    public static double duration2Double(String duration){
        switch (duration){
            case "1": return 1;
            case "1/2": return 0.5;
            case "1/4": return 0.25;
            case "1/8": return 0.125;
            default: return -1;
        }
    }
}
