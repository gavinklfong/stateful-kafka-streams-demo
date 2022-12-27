package space.gavinklfong.demo.insurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import space.gavinklfong.demo.insurance.dto.ClaimRequest;
import space.gavinklfong.demo.insurance.dto.Priority;
import space.gavinklfong.demo.insurance.dto.Product;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class TestController {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @GetMapping("/test")
    public ResponseEntity<String> generateClaimRequest() throws ExecutionException, InterruptedException {
        ClaimRequest request = ClaimRequest.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .product(Product.MEDICAL)
                .claimAmount(100d)
                .priority(Priority.HIGH)
                .build();

        kafkaTemplate.send("claim-submitted", request).get();

        return ResponseEntity.ok().build();
    }
}
