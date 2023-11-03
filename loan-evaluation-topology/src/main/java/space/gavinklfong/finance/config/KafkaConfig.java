package space.gavinklfong.finance.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic loanResultTopic(@Value("${loan.output.topic}") String outputTopic) {
        return TopicBuilder.name(outputTopic).build();
    }

    @Bean
    public RecordMessageConverter recordMessageConverter() {
        return new ByteArrayJsonMessageConverter();
    }

}
