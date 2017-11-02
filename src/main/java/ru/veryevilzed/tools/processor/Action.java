package ru.veryevilzed.tools.processor;

import lombok.Getter;
import lombok.Setter;

public abstract class Action<T> implements Comparable<Action> {

    @Getter
    @Setter
    int order = 0;

    @Override
    public int compareTo(Action o) {
        return this.order - o.order;
    }

    public abstract void action(T item);
}

