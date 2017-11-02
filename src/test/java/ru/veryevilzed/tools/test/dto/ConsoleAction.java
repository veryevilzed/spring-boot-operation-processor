package ru.veryevilzed.tools.test.dto;

import ru.veryevilzed.tools.dto.Action;

public class ConsoleAction extends Action<Transaction> {

    @Override
    public void action(Transaction item) {
        System.out.printf("value:%s%n", item.getValue() );
    }
}
