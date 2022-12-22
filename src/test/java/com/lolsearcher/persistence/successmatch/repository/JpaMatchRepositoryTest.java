package com.lolsearcher.persistence.successmatch.repository;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.entity.match.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
public class JpaMatchRepositoryTest {

    @Autowired
    EntityManager em;

    private MatchRepository matchRepository;

    @BeforeEach
    public void setup(){
        matchRepository = new JpaMatchRepository(em);
    }


    @ParameterizedTest
    @DisplayName("Entity 설정에 맞게 영속성 컨텍스트에 저장된다.")
    @MethodSource("com.lolsearcher.persistence.successmatch.repository.JpaMatchRepositoryTestUpSet#getMatch")
    public void saveTestInPersistenceContext(Match match){
        //given
        List<String> members = new ArrayList<>();
        for(Member member : match.getMembers()){
            members.add(member.getSummonerId());
        }

        //when
        matchRepository.save(match);

        //then
        Match persistenceMatch = matchRepository.findById(match.getMatchId());

        assertThat(persistenceMatch.getMatchId()).isEqualTo(match.getMatchId());
        for(Member dbMember : persistenceMatch.getMembers()){
            assertThat(dbMember.getSummonerId()).isIn(members);
        }
    }

    @Transactional
    @ParameterizedTest
    @MethodSource("com.lolsearcher.persistence.successmatch.repository.JpaMatchRepositoryTestUpSet#getMatch")
    @DisplayName("영속성 컨텍스트에 똑같은 데이터를 중복으로 저장 시도해도 예외가 발생하지 않는다.")
    public void saveTestInPersistenceContextWithDuplicatedData(Match match){
        //given
        List<String> members = new ArrayList<>();
        for(Member member : match.getMembers()){
            members.add(member.getSummonerId());
        }

        //when
        matchRepository.save(match);
        matchRepository.save(match);
        matchRepository.save(match);

        //then
        Match persistenceMatch = matchRepository.findById(match.getMatchId());

        assertThat(persistenceMatch.getMatchId()).isEqualTo(match.getMatchId());
        for(Member dbMember : persistenceMatch.getMembers()){
            assertThat(dbMember.getSummonerId()).isIn(members);
        }
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"matchId1", "matchId2"})
    @DisplayName("존재하지 않는 매치 ID로 조회하면 NULL을 반환한다.")
    public void findByIdTestWithIncorrectId(String matchId){
        //given

        //when
        Match persistenceMatch = matchRepository.findById(matchId);

        //then
        assertThat(persistenceMatch).isNull();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("매치 ID를 null로 조회하면 예외가 발생한다.")
    public void findByIdTestWithNull(String matchId){

        //when & then
        assertThrows(IllegalArgumentException.class, ()-> matchRepository.findById(matchId));
    }
}
