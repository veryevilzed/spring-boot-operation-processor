package ru.veryevilzed.tools.test.dto;

import lombok.Data;
import ru.veryevilzed.tools.dto.Modificator;

@Data
public class ValueModificator extends Modificator<Transaction> {

    int multiply = 1;

    @Override
    public Transaction modificate(Transaction item) {
        item.setValue(item.getValue() * multiply);
        return item;
    }

}
