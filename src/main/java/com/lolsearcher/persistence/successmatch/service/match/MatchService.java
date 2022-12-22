package com.lolsearcher.persistence.successmatch.service.match;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
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
            matchRepository.save(match);
        }
    }
}
