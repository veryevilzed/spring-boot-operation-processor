package ru.veryevilzed.tools.test.process;

import lombok.Data;
import ru.veryevilzed.tools.processor.Modifier;

@Data
public class ValueModifier extends Modifier<Transaction> {

    int multiply = 1;

    @Override
    public Transaction modify(Transaction item) {
        item.setValue(item.getValue() * multiply);
        return item;
    }

}
