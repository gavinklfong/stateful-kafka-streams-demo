package space.gavinklfong.demo.insurance.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Counter approvedClaimCounter(MeterRegistry meterRegistry) {
        return meterRegistry.counter("claim.result", "status", "approved");
    }

    public Counter needFollowupClaimCounter(MeterRegistry meterRegistry) {
        return meterRegistry.counter("claim.result", "status", "need-followup");
    }

    public Counter declinedClaimCounter(MeterRegistry meterRegistry) {
        return meterRegistry.counter("claim.result", "status", "declined");
    }
}
