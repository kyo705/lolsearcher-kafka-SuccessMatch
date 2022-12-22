package com.lolsearcher.persistence.successmatch.repository;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.entity.match.Member;
import com.lolsearcher.persistence.successmatch.entity.match.MemberCompKey;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class JpaMatchRepositoryTestUpSet {

    protected static Stream<Arguments> getMatch(){
        Match match = new Match();
        match.setMatchId("matchId1");

        for(int i=0;i<10;i++){
            Member member = new Member();

            member.setCk(new MemberCompKey(match.getMatchId(), i));
            member.setSummonerId("user" + i);

            member.setMatch(match);
        }

        return Stream.of(arguments(match));
    }
}
