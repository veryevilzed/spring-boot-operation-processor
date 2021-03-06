package ru.veryevilzed.tools.test.process;

import lombok.Data;
import ru.veryevilzed.tools.processor.Filter;

import java.util.Set;

/**
 * Демо фильтр проверки валюты
 */
@Data
public class CurrencyFilter extends Filter<Transaction> {

    Set<String> currency;

    @Override
    public boolean filter(Transaction i) {
        return currency.contains("ANY") || currency.contains(i.getCurrency());
    }
}
