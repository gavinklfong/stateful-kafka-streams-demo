package space.gavinklfong.demo.insurance.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.insurance.dto.ClaimRequest;
import space.gavinklfong.demo.insurance.model.ClaimReviewResult;
import space.gavinklfong.demo.insurance.service.ClaimReviewService;

import static space.gavinklfong.demo.insurance.config.KafkaConfig.CLAIM_SUBMITTED_TOPIC;
import static space.gavinklfong.demo.insurance.config.KafkaConfig.CLAIM_UPDATED_TOPIC;

@Slf4j
@Component
public class ClaimReqEventListener {
    @Autowired
    private ClaimReviewService claimReviewService;
    @Autowired
    private KafkaTemplate<String, ClaimReviewResult> kafkaTemplate;

    @KafkaListener(id = "new-claim-handler", topics = CLAIM_SUBMITTED_TOPIC)
    public void handleClaimRequestEvent(ClaimRequest claimRequest)  {
        log.info("Claim request received: {}", claimRequest);
        ClaimReviewResult result = claimReviewService.processClaimRequest(claimRequest);
        kafkaTemplate.send(CLAIM_UPDATED_TOPIC, result.getCustomerId(), result);
    }
}

