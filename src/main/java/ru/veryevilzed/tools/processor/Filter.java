package ru.veryevilzed.tools.processor;

/**
 * Фильтры для выполнения данной операции
 */
public abstract class Filter<T> extends AbstractComparableOperationPart {

    public abstract boolean filter(T item);
}
