package space.gavinklfong.demo.insurance.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@EnableKafka
@Configuration
public class KafkaConfig {
    public static final String CLAIM_SUBMITTED_TOPIC = "claim-submitted";
    public static final String CLAIM_UPDATED_TOPIC = "claim-updated";

    @Bean
    public NewTopic claimSubmittedTopic() {
        return TopicBuilder.name(CLAIM_SUBMITTED_TOPIC).build();
    }

    @Bean
    public NewTopic claimUpdatedTopic() {
        return TopicBuilder.name(CLAIM_UPDATED_TOPIC).build();
    }

    @Bean
    public RecordMessageConverter recordMessageConverter() {
        return new ByteArrayJsonMessageConverter();
    }
}
