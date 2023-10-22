package space.gavinklfong.finance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.finance.schema.EvaluationResult;
import space.gavinklfong.demo.finance.schema.LoanRequest;
import space.gavinklfong.demo.finance.schema.LoanResponse;
import space.gavinklfong.finance.dto.AccountBalance;

import java.math.BigDecimal;
import java.time.Instant;

import static org.apache.commons.lang3.compare.ComparableUtils.is;

@RequiredArgsConstructor
@Component
public class LoanEvaluationService {

    private final AccountBalanceRepo accountBalanceRepo;

    public LoanResponse evaluate(LoanRequest loanRequest) {
        EvaluationResult result = accountBalanceRepo.findById(loanRequest.getAccount())
                .map(balance -> evaluate(loanRequest, balance))
                .orElse(EvaluationResult.REJECTED);

        return LoanResponse.newBuilder()
                .setRequestId(loanRequest.getRequestId())
                .setAccount(loanRequest.getAccount())
                .setAmount(loanRequest.getAmount())
                .setResult(result)
                .setTimestamp(Instant.now())
                .build();
    }

    private EvaluationResult evaluate(LoanRequest loanRequest, AccountBalance accountBalance) {
        if (is(accountBalance.getAmount())
                .greaterThanOrEqualTo(loanRequest.getAmount().multiply(BigDecimal.valueOf(3)))) {
            return EvaluationResult.APPROVED;
        } else if (is(accountBalance.getAmount())
                .greaterThanOrEqualTo(loanRequest.getAmount().multiply(BigDecimal.valueOf(2)))) {
            return EvaluationResult.REVIEW_NEEDED;
        } else {
            return EvaluationResult.REJECTED;
        }
    }
}
