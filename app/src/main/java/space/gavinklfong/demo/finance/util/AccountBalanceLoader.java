package space.gavinklfong.demo.finance.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.springframework.core.io.ClassPathResource;
import space.gavinklfong.demo.finance.model.AccountBalance;

import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountBalanceLoader {
    private static final CSVFormat CSV_FORMAT = CSVFormat.RFC4180.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();
    @SneakyThrows
    public static List<AccountBalance> loadAccountBalances(String filename){
        try (Reader in = new FileReader(new ClassPathResource(filename).getFile())) {
            return CSV_FORMAT.parse(in).stream()
                    .map(csvRecord -> AccountBalance.builder()
                            .timestamp(parseDateTime(csvRecord.get(0)))
                            .account(csvRecord.get(1))
                            .amount(new BigDecimal(csvRecord.get(2)))
                            .build())
                    .toList();
        }
    }

    private static LocalDateTime parseDateTime(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
