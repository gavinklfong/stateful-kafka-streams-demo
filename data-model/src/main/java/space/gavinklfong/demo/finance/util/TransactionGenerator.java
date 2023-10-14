package space.gavinklfong.demo.finance.util;

import org.apache.commons.lang3.RandomUtils;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionType;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionGenerator {
    private static final int AMOUNT_MIN = 100;
    private static final int AMOUNT_MAX = 10000;
    private static final int QUANTITY_MIN = 1;
    private static final int QUANTITY_MAX = 5;
    private static final int PRODUCT_MAX = 5;
    private static final LocalDate DATE_MIN = LocalDate.parse("2022-01-01");
    private static final LocalDate DATE_MAX = LocalDate.parse("2022-12-31");

    private TransactionGenerator() { }

    public static Transaction generateRandomTransaction() {
        TransactionType transactionType = generateTransactionType();
        return switch (transactionType) {
            case TRANSFER -> generateTransfer();
            case FEE -> generateFee();
            case DEPOSIT -> generateDeposit();
            case INTEREST -> generateInterest();
            case WITHDRAWAL -> generateWithdrawal();
        };
    }

    private static Transaction generateTransfer() {
        return Transaction.builder().build();
    }

    private static Transaction generateFee() {
        return Transaction.builder().build();
    }

    public static Transaction generateDeposit() {
        return Transaction.builder().build();
    }

    private static Transaction generateInterest() {
        return Transaction.builder().build();
    }

    private static Transaction generateWithdrawal() {
        return Transaction.builder().build();
    }


    private static TransactionType generateTransactionType() {
        return TransactionType.values()[RandomUtils.nextInt(0, TransactionType.values().length)];
    }

    private static LocalDate generatePostingDate() {
        return betweenDate(DATE_MIN, DATE_MAX);
    }

    private static LocalDate betweenDate(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
