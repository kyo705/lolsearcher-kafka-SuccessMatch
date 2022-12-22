package com.lolsearcher.persistence.successmatch.service.kafka.consumer;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.service.match.MatchService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchConsumerService{

    private final MatchService matchService;

    @KafkaListener(
            topics = {"${app.kafka.topics.success_match.name}"},
            groupId = "${app.kafka.consumers.success_match.group_id}",
            containerFactory = "${app.kafka.consumers.success_match.container_factory}"
    )
    public void consume(ConsumerRecords<String, Match> records, Acknowledgment acknowledgment) {

        List<Match> matches = new ArrayList<>(records.count());

        for(ConsumerRecord<String, Match> record : records){
            Match match = record.value();

            matches.add(match);
        }
        //매치 데이터들 배치처리로 db에 한번에 저장 => 하나의 트랜잭션으로 묶음
        matchService.save(matches);

        //마지막 오프셋 커밋 => ackmode = manual-immediate
        acknowledgment.acknowledge();
    }
}
