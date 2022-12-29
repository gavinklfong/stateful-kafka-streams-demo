package space.gavinklfong.demo.insurance.service;

import io.micrometer.core.instrument.Counter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.gavinklfong.demo.insurance.dto.ClaimRequest;
import space.gavinklfong.demo.insurance.dto.Priority;
import space.gavinklfong.demo.insurance.dto.Product;
import space.gavinklfong.demo.insurance.model.ClaimReviewResult;
import space.gavinklfong.demo.insurance.model.Status;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class ClaimReviewServiceTest {

    @Mock
    private Counter approvedClaimCounter;
    @Mock
    private Counter needFollowupClaimCounter;
    @Mock
    private Counter declinedClaimCounter;
    @InjectMocks
    private ClaimReviewService claimReviewService;

    @Test
    void givenMedicalClaimWithSmallAmount_whenRunClaimProcess_thenReturnApprovedStatus() {
        ClaimRequest request = ClaimRequest.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .priority(Priority.MEDIUM)
                .claimAmount(100d)
                .product(Product.MEDICAL)
                .build();

        ClaimReviewResult result = claimReviewService.processClaimRequest(request);

        assertClaimReviewResult(result, request, Status.APPROVED);
    }

    @Test
    void givenMedicalClaimWithLargeAmount_whenRunClaimProcess_thenReturnDeclinedStatus() {
        ClaimRequest request = ClaimRequest.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .priority(Priority.MEDIUM)
                .claimAmount(5000d)
                .product(Product.MEDICAL)
                .build();

        ClaimReviewResult result = claimReviewService.processClaimRequest(request);

        assertClaimReviewResult(result, request, Status.DECLINED);
    }

    @Test
    void givenNonMedicalClaim_whenRunClaimProcess_thenReturnNeedFollowUpStatus() {
        ClaimRequest request = ClaimRequest.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .priority(Priority.MEDIUM)
                .claimAmount(100d)
                .product(Product.HOME)
                .build();

        ClaimReviewResult result = claimReviewService.processClaimRequest(request);

        assertClaimReviewResult(result, request, Status.NEED_FOLLOW_UP);
    }

    private void assertClaimReviewResult(ClaimReviewResult result, ClaimRequest request, Status expectedStatus) {
        assertThat(result.getClaimId()).isEqualTo(request.getId());
        assertThat(result.getCustomerId()).isEqualTo(request.getCustomerId());
        assertThat(result.getStatus()).isEqualTo(expectedStatus);
    }
}
