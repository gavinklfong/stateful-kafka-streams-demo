package space.gavinklfong.finance.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "account_balances")
public class AccountBalance {
    @Id
    private String account;
    private BigDecimal amount;
}
