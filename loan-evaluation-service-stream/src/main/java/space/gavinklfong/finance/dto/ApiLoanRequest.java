package space.gavinklfong.finance.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ApiLoanRequest {
    String account;
    BigDecimal amount;
}
