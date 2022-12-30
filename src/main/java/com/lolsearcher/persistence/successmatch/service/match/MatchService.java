package com.lolsearcher.persistence.successmatch.service.match;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.entity.match.Member;
import com.lolsearcher.persistence.successmatch.entity.match.PerkStats;
import com.lolsearcher.persistence.successmatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final MatchRepository matchRepository;

    @Transactional
    public void save(List<Match> matches) {
        for(Match match : matches){
            if(matchRepository.findById(match.getMatchId()) != null){
                continue;
            }
            addAdditionalValue(match);

            matchRepository.save(match);
        }
    }


    /**
     *    PERKSTATS 테이블로부터 특정 값에 해당하는 PK 찾아 해당 PK 값 찾는 메소드
     */
    private void addAdditionalValue(Match successMatch) {
        List<Member> members = successMatch.getMembers();

        for(Member member : members){
            PerkStats perkStats = member.getPerks().getPerkStats();

            PerkStats dbPerkStats =
                    matchRepository.findPerkStats(perkStats.getDefense(), perkStats.getFlex(), perkStats.getOffense());

            member.getPerks().setPerkStatsId(dbPerkStats.getId());
        }
    }
}
