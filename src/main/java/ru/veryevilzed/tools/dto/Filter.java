package ru.veryevilzed.tools.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.function.Predicate;

/**
 * Фильтры для выполнения данной операции
 */
public abstract class Filter<T> implements Comparable<Filter> {

    @Getter @Setter
    int order = 0;

    @Override
    public int compareTo(Filter o) {
        return this.order - o.order;
    }

    public abstract boolean filter(T item);

}
