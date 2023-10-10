package space.gavinklfong.demo.finance.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class TransactionKey {
    UUID id;
    String fromAccount;
    String toAccount;
}
