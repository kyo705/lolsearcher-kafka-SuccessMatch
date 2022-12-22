package com.lolsearcher.persistence.successmatch.config;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SuccessMatchConsumerConfig {

    //listenerContainerFactory 설정
    private final int CONCURRENCY_COUNT = 1; //멀티 컨슈머로 처리하면 안됌 -> 장애 발생 시 데이터 유실 및 중복 발생

    //ConsumerFactory 설정
    @Value("${app.kafka.zookeeper.clusters.lolsearcher.brokers.broker1.server}")
    private String BOOTSTRAP_SERVER;
    @Value("${app.kafka.consumers.success_match.trust_packages}")
    private String TRUST_PACKAGES;
    @Value("${app.kafka.consumers.success_match.poll_record_size}")
    private int POLL_RECORDS_COUNT; /* riot api 요청 제한 횟수가 2분당 최대 100회 */
    @Value("${app.kafka.consumers.success_match.heartbeat}")
    private int HEARTBEAT_MS;
    @Value("${app.kafka.consumers.success_match.session_time_out}")
    private int SESSION_TIME_OUT_MS; /* 2min + heartbeat time */
    @Value("${app.kafka.consumers.success_match.auto_offset_reset}")
    private String AUTO_OFFSET_RESET;
    @Value("${app.kafka.consumers.success_match.isolation}")
    private String ISOLATION_LEVEL;
    @Value("${app.kafka.consumers.success_match.enable_auto_commit}")
    private boolean AUTO_OFFSET_COMMIT;


    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Match>> successMatchListenerContainerFactory(
            DefaultKafkaConsumerFactory<String, Match> consumerFactory
    ){
        //리스너컨테이너 팩토리 생성
        ConcurrentKafkaListenerContainerFactory<String, Match> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();

        listenerFactory.setConsumerFactory(consumerFactory);
        listenerFactory.setConcurrency(CONCURRENCY_COUNT);
        listenerFactory.setBatchListener(true);

        listenerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        listenerFactory.getContainerProperties().setConsumerRebalanceListener(successMatchRebalanceListener());

        return listenerFactory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Match> successMatchConsumerFactory() {

        //컨슈머 팩토리 생성
        Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        properties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, TRUST_PACKAGES);

        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, POLL_RECORDS_COUNT);
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, ISOLATION_LEVEL);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, AUTO_OFFSET_COMMIT);
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, HEARTBEAT_MS);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, SESSION_TIME_OUT_MS);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConsumerRebalanceListener successMatchRebalanceListener() {

        //noinspection NullableProblems
        return new ConsumerAwareRebalanceListener() {
            @Override
            public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
                //nothing to do -> 개별 레코드를 하나하나 커밋했기 때문
            }

            @Override
            public void onPartitionsRevokedAfterCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
                //nothing to do
            }

            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                //nothing to do
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                //새롭게 할당받은 파티션
            }
        };
    }
}
