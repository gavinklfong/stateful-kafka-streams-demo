package space.gavinklfong.demo.insurance.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import space.gavinklfong.demo.insurance.dto.ClaimRequest;
import space.gavinklfong.demo.insurance.dto.Priority;
import space.gavinklfong.demo.insurance.dto.Product;
import space.gavinklfong.demo.insurance.model.ClaimReviewResult;
import space.gavinklfong.demo.insurance.model.Status;
import space.gavinklfong.demo.insurance.service.ClaimReviewService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringJUnitConfig
@ContextConfiguration(classes = {ClaimReqEventListener.class})
@Tag("UnitTest")
class ClaimReqEventListenerTest {

    @MockBean
    private KafkaTemplate<String, ClaimReviewResult> kafkaTemplate;

    @MockBean
    private ClaimReviewService claimReviewService;

    @Autowired
    private ClaimReqEventListener claimReqEventListener;

    @Test
    void whenClaimRequestArrived_thenInvokeClaimProcessServiceAndClaimStatusUpdateGateway() {

        when(claimReviewService.processClaimRequest((any(ClaimRequest.class)))).thenAnswer(invocation -> {
            ClaimRequest request = invocation.getArgument(0);
            return ClaimReviewResult.builder()
                    .claimId(request.getId())
                    .customerId(request.getCustomerId())
                    .status(Status.APPROVED)
                    .build();
        });

        ClaimRequest request = ClaimRequest.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .product(Product.MEDICAL)
                .claimAmount(100d)
                .priority(Priority.HIGH)
                .build();

        claimReqEventListener.handleClaimRequestEvent(request);

        verify(kafkaTemplate).send(eq("claim-updated"), eq(request.getCustomerId()), any(ClaimReviewResult.class));
    }

}
