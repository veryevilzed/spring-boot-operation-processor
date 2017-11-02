package ru.veryevilzed.tools.test.process;

import ru.veryevilzed.tools.processor.Action;

public class ConsoleAction extends Action<Transaction> {

    @Override
    public void action(Transaction item) {
        System.out.printf("value:%s%n", item.getValue() );
    }
}
