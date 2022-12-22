package com.lolsearcher.persistence.successmatch.repository;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class JpaMatchRepository implements MatchRepository{

    private final EntityManager em;

    @Override
    public void save(Match match) {
        em.persist(match);
    }

    @Override
    public Match findById(String matchId) {
        return em.find(Match.class, matchId);
    }
}
