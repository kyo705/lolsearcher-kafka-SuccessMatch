package com.lolsearcher.persistence.successmatch.service.consumer;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.service.match.MatchService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@EmbeddedKafka(
        brokerProperties = {
            "listeners=PLAINTEXT://localhost:9092",
            "offsets.topic.replication.factor=1",
            "transaction.state.log.replication.factor=1",
            "transaction.state.log.min.isr=1"}
)
@SpringBootTest
@DirtiesContext
public class SuccessMatchConsumerServiceUnitTest {

    @Value("${app.kafka.consumers.success_match.poll_record_size}")
    private int poll_size;

    private int consuming_record_count;

    @MockBean
    private MatchService matchService;

    @DisplayName("@kafkaListener를 통해 토픽 내 레코드가 컨슈밍된다.")
    @Test
    public void consumeTest() throws InterruptedException {

        //given
        List<ProducerRecord<String, Match>> producerRecords = SuccessMatchConsumerTestSetUp.getSuccessMatchIdRecord();

        consuming_record_count = producerRecords.size();

        willAnswer(invocation -> {
            List<Match> matches = invocation.getArgument(0);

            if(consuming_record_count > poll_size){
                assertThat(matches.size()).isEqualTo(poll_size);
                consuming_record_count -= poll_size;
            }else{
                assertThat(matches.size()).isEqualTo(consuming_record_count);
                consuming_record_count = 0;
            }
            return null;
        }).given(matchService).save(any());

        //when
        SuccessMatchConsumerTestSetUp.sendProducerRecords(producerRecords);

        Thread.sleep(5000);

        //then
        verify(matchService, times(producerRecords.size()/ poll_size + 1)).save(any());
    }
}
