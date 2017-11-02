package ru.veryevilzed.tools.test;

import org.junit.Test;
import ru.veryevilzed.tools.dto.Operations;
import ru.veryevilzed.tools.test.dto.Transaction;

import java.io.File;
import java.io.IOException;

public class OperationExecutorTest {

    @Test
    public void operationFilter() throws IOException {

        Transaction t = new Transaction();
        t.setId(10);
        t.setCurrency("EUR");
        t.setValue(500);


        Operations<Transaction> operations = new Operations<>(
                new File("example/process.yml"),
                Transaction.class,
                "ru.veryevilzed.tools.test.dto"
        );
        operations.exec(t);
    }
}