package com.lolsearcher.persistence.successmatch.service.consumer;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SuccessMatchConsumerTestSetUp {

    private static final String TOPIC_NAME = "success_match";

    protected static List<ProducerRecord<String, Match>> getSuccessMatchIdRecord() {

        List<ProducerRecord<String, Match>> records = new ArrayList<>(110);

        for(int i=0;i<110;i++){
            Match match = new Match();
            match.setMatchId("matchId" + i);

            records.add(new ProducerRecord<>(TOPIC_NAME, match));
        }

        return records;
    }


    protected static void sendProducerRecords(List<ProducerRecord<String, Match>> records) {
        Map<String, Object> properties = createProperties();

        ProducerFactory<String, Match> producerFactory = new DefaultKafkaProducerFactory<>(properties);

        KafkaTransactionManager<String, Match> transactionManager = new KafkaTransactionManager<>(producerFactory);
        KafkaTemplate<String, Match> kafkaTemplate = new KafkaTemplate<>(producerFactory);


        //트랜잭션 시작
        TransactionStatus transactionStatus = transactionManager.getTransaction(null);
        try{
            for(ProducerRecord<String, Match> record : records){
                kafkaTemplate.send(record);
            }

            //트랜잭션 종료
            transactionStatus.flush();
            transactionManager.commit(transactionStatus);
        }catch (Exception e){
            transactionManager.rollback(transactionStatus);
        }
    }


    private static Map<String, Object> createProperties(){
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "success_match");

        return properties;
    }
}
