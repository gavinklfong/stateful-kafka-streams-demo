package space.gavinklfong.demo.finance.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Builder
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Value
public class TransactionKey {
    UUID id;
    String fromAccount;
    String toAccount;
}
