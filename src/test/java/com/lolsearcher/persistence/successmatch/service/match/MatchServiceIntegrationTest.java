package com.lolsearcher.persistence.successmatch.service.match;


import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.entity.match.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MatchServiceIntegrationTest {

    @Autowired
    EntityManager em;

    @Autowired
    private MatchService matchService;

    @ParameterizedTest
    @MethodSource("com.lolsearcher.persistence.successmatch.service.match.MatchServiceTestUpSet#getMatches")
    @DisplayName("Entity 객체를 db에 정확히 한 번 저장한다.")
    public void saveTest(List<Match> matches){
        //given

        //when
        matchService.save(matches);

        //then
        for(Match match : matches){
            Match dbMatch = em.find(Match.class, match.getMatchId());

            assertThat(dbMatch).isNotNull();

            assertThat(dbMatch.getMatchId()).isEqualTo(match.getMatchId());

            for(int i=0;i<10;i++){
                Member dbMatchMember = dbMatch.getMembers().get(i);
                Member member = match.getMembers().get(i);

                assertThat(dbMatchMember.getSummonerName()).isEqualTo(member.getSummonerName());
                assertThat(dbMatchMember.getPerks().getMainPerk1()).isEqualTo(member.getPerks().getMainPerk1());
            }
        }
    }

    @Test
    @DisplayName("적절하지 않는 데이터가 전달될 경우 예외가 발생한다.")
    public void saveTestWithImproperParameter(){

        //when & then
        Assertions.assertThrows(NullPointerException.class, ()->{
            matchService.save(null);
        });
    }
}
