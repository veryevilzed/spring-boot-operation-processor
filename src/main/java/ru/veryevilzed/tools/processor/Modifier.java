package ru.veryevilzed.tools.processor;

import lombok.Getter;
import lombok.Setter;

public abstract class Modifier<T> implements Comparable<Modifier> {

    @Getter
    @Setter
    private int order = 0;

    @Getter
    @Setter
    private String name = "";

    @Getter
    @Setter
    private String suffix = "";

    @Override
    public int compareTo(Modifier o) {
        return this.order - o.order;
    }

    public abstract T modify(T item);
}
