package ru.veryevilzed.tools.test.dto;


import lombok.Data;
import ru.veryevilzed.tools.dto.Filter;

@Data
public class MinValueFilter extends Filter<Transaction> {

    long minValue = 0;
    long maxValue = 0;

    @Override
    public boolean filter(Transaction i) {
        return  i.getValue() >= minValue && i.getValue() <= maxValue;
    }
}
