package ru.veryevilzed.tools.processor;

import lombok.Getter;
import lombok.Setter;

public abstract class Modificator<T> implements Comparable<Modificator> {

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
    public int compareTo(Modificator o) {
        return this.order - o.order;
    }

    public abstract T modify(T item);
}
