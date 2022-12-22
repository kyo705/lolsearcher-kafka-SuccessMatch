package com.lolsearcher.persistence.successmatch.service.match;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MatchServiceUnitTest {

    @Mock
    private MatchRepository matchRepository;

    private MatchService matchService;

    @BeforeEach
    public void upSet(){
        matchService = new MatchService(matchRepository);
    }

    @ParameterizedTest
    @MethodSource("com.lolsearcher.persistence.successmatch.service.match.MatchServiceTestUpSet#getMatches")
    @DisplayName("파라미터로 전달 받은 match 데이터들을 데이터베이스에 저장한다.")
    public void saveTest(List<Match> matches){
        //given
        for(Match match : matches){
            given(matchRepository.findById(match.getMatchId())).willReturn(null);
        }

        //when
        matchService.save(matches);

        //then
        verify(matchRepository, times(matches.size())).save(any(Match.class));
    }
}
