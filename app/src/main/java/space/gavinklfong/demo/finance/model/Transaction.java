package space.gavinklfong.demo.finance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Transaction {
    UUID id;
    String fromAccount;
    String toAccount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
    BigDecimal amount;
    TransactionType type;
}
