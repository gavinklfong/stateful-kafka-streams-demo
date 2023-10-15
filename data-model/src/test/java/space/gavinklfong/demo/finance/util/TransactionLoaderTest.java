package space.gavinklfong.demo.finance.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.util.TransactionLoader;

import java.util.List;

@Slf4j
class TransactionLoaderTest {

    private static final String CSV_FILE = "/transactions-test.csv";

    @Test
    void loadCSV() {
        List<Transaction> transactions = TransactionLoader.loadTransactions(CSV_FILE);
        transactions.forEach(t -> log.info("{}", t));
    }
}
