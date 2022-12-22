package com.lolsearcher.persistence.successmatch.repository;

import com.lolsearcher.persistence.successmatch.entity.match.Match;

public interface MatchRepository {

    void save(Match match);

    Match findById(String matchId);
}
