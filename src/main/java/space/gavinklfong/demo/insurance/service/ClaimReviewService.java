package space.gavinklfong.demo.insurance.service;

import com.github.javafaker.Faker;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import space.gavinklfong.demo.insurance.dto.ClaimRequest;
import space.gavinklfong.demo.insurance.dto.Product;
import space.gavinklfong.demo.insurance.model.ClaimReviewResult;
import space.gavinklfong.demo.insurance.model.Status;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClaimReviewService {

    private static final int PROCESS_TIME_LOWER_BOUND = 500;

    private final Counter approvedClaimCounter;
    private final Counter needFollowupClaimCounter;
    private final Counter declinedClaimCounter;


    public ClaimReviewResult processClaimRequest(ClaimRequest claimRequest) {
        log.info("Claim request processing - start - {}", claimRequest.toString());
        long start = System.currentTimeMillis();

        // Simulate claim process
        try {
            Thread.sleep(RandomUtils.nextInt(PROCESS_TIME_LOWER_BOUND, 500));

            Faker faker = new Faker();

            Status claimStatus = Status.NEED_FOLLOW_UP;
            if (claimRequest.getProduct() == Product.MEDICAL) {
                if (claimRequest.getClaimAmount() < 5000) {
                    claimStatus = Status.APPROVED;
                } else {
                    claimStatus = Status.DECLINED;
                }
            }

            ClaimReviewResult result = ClaimReviewResult.builder()
                    .customerId(claimRequest.getCustomerId())
                    .claimId(claimRequest.getId())
                    .status(claimStatus)
                    .product(claimRequest.getProduct())
                    .claimAmount(claimRequest.getClaimAmount())
                    .remarks(faker.lorem().sentence())
                    .build();

            updateClaimStatusMetric(result);

            long end = System.currentTimeMillis();
            log.info("Claim request processing - end - processing time {}, claim = {}, status = {}", (end - start), claimRequest.toString(), result.getStatus());

            return result;

        } catch (InterruptedException e) {
            long end = System.currentTimeMillis();
            log.error("claim process failed. processing time = {}, claim = {}", (end - start), claimRequest);
            throw new RuntimeException("claim process failed. id = " + claimRequest.getId());
        }
    }

    private void updateClaimStatusMetric(ClaimReviewResult result) {
        if (result.getStatus().equals(Status.APPROVED)) {
            approvedClaimCounter.increment();
        } else if (result.getStatus().equals(Status.DECLINED)) {
            declinedClaimCounter.increment();
        } else if (result.getStatus().equals(Status.NEED_FOLLOW_UP)) {
            needFollowupClaimCounter.increment();
        }
    }
}
