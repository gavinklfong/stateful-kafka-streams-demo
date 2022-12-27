package space.gavinklfong.demo.insurance.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.insurance.model.ClaimReviewResult;

@Slf4j
@Component
public class ClaimReviewResultEventListener {

    @KafkaListener(id = "claimUpdateHandler", topics = "claim-updated")
    public void handleClaimRequestEvent(ClaimReviewResult claimReviewResult) {
        log.info("Claim result generated: " + claimReviewResult);
    }

}

