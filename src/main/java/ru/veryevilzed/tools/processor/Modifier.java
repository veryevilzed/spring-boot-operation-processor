package ru.veryevilzed.tools.processor;

public abstract class Modifier<T> extends AbstractComparableOperationPart {

    public abstract T modify(T item);
}
