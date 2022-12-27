package space.gavinklfong.demo.insurance.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.insurance.dto.ClaimRequest;
import space.gavinklfong.demo.insurance.model.ClaimReviewResult;
import space.gavinklfong.demo.insurance.service.ClaimReviewService;

@Slf4j
@Component
public class ClaimReqEventListener {

    @Autowired
    private ClaimReviewService claimReviewService;

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @KafkaListener(id = "newClaimHandler", topics = "claim-submitted")
    public void handleClaimRequestEvent(ClaimRequest claimRequest)  {
        log.info("Claim request received: {}", claimRequest);
        ClaimReviewResult result = claimReviewService.processClaimRequest(claimRequest);
        kafkaTemplate.send("claim-updated", result);
//        kafkaTemplate.flush();
    }

}

