package com.buran911.utils;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Автоматический закрыватель для нескольких объектов.
 */
public class AutoCloser implements Closeable {
    // Объекты, которые нужно закрывать.
    private List<AutoCloseable> objects = new LinkedList<>();

    /**
     * Сквозное добавление объекта в список закрываемых объектов.
     */
    public <TYPE extends AutoCloseable> TYPE register(TYPE object) {
        Optional.ofNullable(object).ifPresent(objects::add);
        return object;
    }

    /**
     * Закрывает все объекты из списка.
     */
    public void close() {
        objects.forEach(AutoCloser::silentClose);
    }

    /**
     * Тихо закрывает объект.
     */
    private static AutoCloseable silentClose(AutoCloseable object) {
        try {
            object.close();
        } catch (Exception ex) {
            // EMPTY
        }
        return object;
    }
}