package com.lolsearcher.persistence.successmatch.repository;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.entity.match.PerkStats;

public interface MatchRepository {

    void save(Match match);

    Match findById(String matchId);

    PerkStats findPerkStats(Short defense, Short flex, Short offense);
}
