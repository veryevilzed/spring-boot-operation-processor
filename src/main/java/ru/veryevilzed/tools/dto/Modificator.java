package ru.veryevilzed.tools.dto;

        import lombok.Getter;
        import lombok.Setter;

public abstract class Modificator<T> implements Comparable<Modificator> {

    @Getter
    @Setter
    int order = 0;

    @Override
    public int compareTo(Modificator o) {
        return this.order - o.order;
    }


    public abstract T modificate(T item);

}
