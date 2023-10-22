package space.gavinklfong.finance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.gavinklfong.demo.finance.schema.LoanRequest;
import space.gavinklfong.finance.dto.ApiLoanRequest;
import space.gavinklfong.finance.service.LoanSender;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/loans")
public class LoanRestController {

    private final LoanSender loanSender;

    @PostMapping
    public ApiLoanRequest submitLoanRequest(@RequestBody ApiLoanRequest apiLoanRequest) {
        LoanRequest loanRequest = LoanRequest.newBuilder()
                .setAccount(apiLoanRequest.getAccount())
                .setAmount(apiLoanRequest.getAmount())
                .setRequestId(UUID.randomUUID().toString())
                .setTimestamp(Instant.now())
                .build();
        loanSender.send(loanRequest);
        return apiLoanRequest;
    }

}
