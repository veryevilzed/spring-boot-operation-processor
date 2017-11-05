package ru.veryevilzed.tools.processor;

import lombok.Getter;
import lombok.Setter;

/**
 * Фильтры для выполнения данной операции
 */
public abstract class Filter<T> implements Comparable<Filter> {

    @Getter @Setter
    int order = 0;

    @Getter @Setter
    String name = "";

    @Getter @Setter
    String suffix = "";

    @Override
    public int compareTo(Filter o) {
        return this.order - o.order;
    }

    public abstract boolean filter(T item);

}
