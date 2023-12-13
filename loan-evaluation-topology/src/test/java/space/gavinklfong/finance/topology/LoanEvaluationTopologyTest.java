package space.gavinklfong.finance.topology;

import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import space.gavinklfong.demo.finance.schema.*;
import space.gavinklfong.demo.finance.topology.SerdeFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
class LoanEvaluationTopologyTest {

    private static final String ACCOUNT_NO = "001-123456-001";

    private static final String TEST_CLASS = LoanEvaluationTopologyTest.class.getName();
    private static final String MOCK_BOOTSTRAP_URL = String.format("%s-bootstrap:1234", TEST_CLASS);
    private static final String MOCK_SCHEMA_REGISTRY_URL = String.format("mock://%s-schema-registry:1234", TEST_CLASS);


    private TopologyTestDriver testDriver;
    private TestInputTopic<Account, AccountBalance> accountBalanceTopic;
    private TestInputTopic<Account, LoanRequest> loanRequestTopic;
    private TestOutputTopic<Account, LoanResponse> loanResponseTopic;

    @BeforeEach
    void setup() throws RestClientException, IOException {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "loan-evaluation");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, MOCK_BOOTSTRAP_URL);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);

        SerdeFactory serdeFactory = new SerdeFactory(MOCK_SCHEMA_REGISTRY_URL, getMockSchemaRegistryClient());

        Topology topology = LoanEvaluationTopology.build(serdeFactory);
        testDriver = new TopologyTestDriver(topology, props);

        Serde<Account> accountSerde = serdeFactory.getSerde(true);
        Serde<LoanRequest> loanRequestSerde = serdeFactory.getSerde(false);
        Serde<AccountBalance> accountBalanceSerde = serdeFactory.getSerde(false);
        Serde<LoanResponse> loanResponseSerde = serdeFactory.getSerde(false);

        loanRequestTopic = testDriver.createInputTopic("loan-requests", accountSerde.serializer(), loanRequestSerde.serializer());
        accountBalanceTopic = testDriver.createInputTopic("account-balances", accountSerde.serializer(), accountBalanceSerde.serializer());
        loanResponseTopic = testDriver.createOutputTopic("loan-evaluation-results", accountSerde.deserializer(), loanResponseSerde.deserializer());
    }

    private MockSchemaRegistryClient getMockSchemaRegistryClient() throws RestClientException, IOException {
        MockSchemaRegistryClient mockSchemaRegistryClient = new MockSchemaRegistryClient();
        mockSchemaRegistryClient.register("account-balances-key", new AvroSchema(Account.SCHEMA$));
        mockSchemaRegistryClient.register("account-balances-value", new AvroSchema(AccountBalance.SCHEMA$));
        mockSchemaRegistryClient.register("loan-requests-key", new AvroSchema(Account.SCHEMA$));
        mockSchemaRegistryClient.register("loan-requests-value", new AvroSchema(LoanRequest.SCHEMA$));
        mockSchemaRegistryClient.register("loan-evaluation-results-key", new AvroSchema(Account.SCHEMA$));
        mockSchemaRegistryClient.register("loan-evaluation-results-value", new AvroSchema(LoanResponse.SCHEMA$));
        return mockSchemaRegistryClient;
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    private static Stream<Arguments> provideLoanAmountAndExpectedResult() {
        return Stream.of(
                Arguments.of(new BigDecimal("900"), new BigDecimal("10"), EvaluationResult.APPROVED),
                Arguments.of(new BigDecimal("900"), new BigDecimal("299"), EvaluationResult.APPROVED),
                Arguments.of(new BigDecimal("900"), new BigDecimal("300"), EvaluationResult.APPROVED),
                Arguments.of(new BigDecimal("900"), new BigDecimal("301"), EvaluationResult.REVIEW_NEEDED),
                Arguments.of(new BigDecimal("900"), new BigDecimal("450"), EvaluationResult.REVIEW_NEEDED),
                Arguments.of(new BigDecimal("900"), new BigDecimal("451"), EvaluationResult.REJECTED)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLoanAmountAndExpectedResult")
    void givenAccountBalance_whenLoanRequest_thenOutputExpectedResult(BigDecimal balanceAmount, BigDecimal loanAmount,
                                                                EvaluationResult expectedResult) {

        Instant accountBalanceTimestamp = Instant.parse("2023-01-01T12:00:00Z");
        Instant loanRequestTimestamp = Instant.parse("2023-01-01T14:00:00Z");

        Account account = Account.newBuilder()
                .setAccount(ACCOUNT_NO)
                .build();

        AccountBalance accountBalance = AccountBalance.newBuilder()
                .setAccount(ACCOUNT_NO)
                .setAmount(balanceAmount)
                .setTimestamp(accountBalanceTimestamp)
                .build();

        accountBalanceTopic.pipeInput(account, accountBalance);

        LoanRequest loanRequest = LoanRequest.newBuilder()
                .setRequestId(UUID.randomUUID().toString())
                .setAccount(ACCOUNT_NO)
                .setAmount(loanAmount)
                .setTimestamp(loanRequestTimestamp)
                .build();
        loanRequestTopic.pipeInput(account, loanRequest);

        List<KeyValue<Account, LoanResponse>> output = loanResponseTopic.readKeyValuesToList();
        assertThat(output).hasSize(1)
                .extracting("value.result")
                .contains(expectedResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.01", "1", "10"})
    void givenNoAccountBalance_whenAnyLoanRequest_thenLoanIsRejected(String loanAmount) {
        Instant loanRequestTimestamp = Instant.parse("2023-01-01T14:00:00Z");

        Account account = Account.newBuilder()
                .setAccount(ACCOUNT_NO)
                .build();

        LoanRequest loanRequest = LoanRequest.newBuilder()
                .setRequestId(UUID.randomUUID().toString())
                .setAccount(ACCOUNT_NO)
                .setAmount(new BigDecimal(loanAmount))
                .setTimestamp(loanRequestTimestamp)
                .build();
        loanRequestTopic.pipeInput(account, loanRequest);

        List<KeyValue<Account, LoanResponse>> output = loanResponseTopic.readKeyValuesToList();
        assertThat(output).hasSize(1)
                .extracting("value.result")
                .contains(EvaluationResult.REJECTED);
    }
}
