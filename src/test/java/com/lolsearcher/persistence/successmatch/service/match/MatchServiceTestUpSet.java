package com.lolsearcher.persistence.successmatch.service.match;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.entity.match.Member;
import com.lolsearcher.persistence.successmatch.entity.match.MemberCompKey;
import com.lolsearcher.persistence.successmatch.entity.match.Perks;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class MatchServiceTestUpSet {

    protected static Stream<Arguments> getMatches(){

        List<Match> matches = new ArrayList<>();

        for(int i=0;i<10;i++){
            Match match = new Match();
            match.setMatchId("matchId" + i);

            for(int j=0;j<10;j++){
                Member member = new Member();
                member.setCk(new MemberCompKey(match.getMatchId(), j));
                member.setSummonerName("user" + j);
                member.setMatch(match);

                Perks perks = new Perks();
                perks.setMemberCompKey(new MemberCompKey(match.getMatchId(), j));
                perks.setMainPerk1((short) j);
                perks.setMember(member);
            }

            matches.add(match);
        }

        return Stream.of(
                arguments(matches)
        );
    }
}
