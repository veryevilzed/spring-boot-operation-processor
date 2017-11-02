package ru.veryevilzed.tools.test.process;

import lombok.Data;

/**
 * Демонстрационная транзакция
 */
@Data
public class Transaction {

    long id;
    int value;
    String currency;
}
