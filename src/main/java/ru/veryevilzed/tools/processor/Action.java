package ru.veryevilzed.tools.processor;

public abstract class Action<T> extends AbstractComparableOperationPart {

    public abstract void action(T item);
}

