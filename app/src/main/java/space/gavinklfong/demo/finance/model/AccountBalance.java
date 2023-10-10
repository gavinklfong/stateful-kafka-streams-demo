package space.gavinklfong.demo.finance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class AccountBalance {
    String account;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
    BigDecimal amount;
}
