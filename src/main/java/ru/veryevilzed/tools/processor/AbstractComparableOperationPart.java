package ru.veryevilzed.tools.processor;

import lombok.Data;

@Data
public abstract class AbstractComparableOperationPart implements Comparable<AbstractComparableOperationPart> {

    private int order = 0;

    private String name = "";

    private String suffix = "";

    @Override
    public int compareTo(AbstractComparableOperationPart o) {
        return this.order - o.order;
    }
}
