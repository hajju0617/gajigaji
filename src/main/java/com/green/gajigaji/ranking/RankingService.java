package com.green.gajigaji.ranking;

import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.ranking.model.GetRankingRes;
import com.green.gajigaji.review.exception.ReviewErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RankingService {
    private final RankingMapper mapper;

    public List<GetRankingRes> getRankingTotal(int limit){
        try {
            return mapper.getRankingTotal(limit);
        } catch (Exception e) {
            log.error("getRankingTotal Error");
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    public List<GetRankingRes> getRankingLocation(int partyLocation, int limit) {
        try {
            return mapper.getRankingLocation(partyLocation, limit);
        } catch (Exception e) {
            log.error("getRankingLocation Error");
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    public List<GetRankingRes> getRankingGenre(int partyGenre, int limit) {
        try {
            return mapper.getRankingGenre(partyGenre, limit);
        } catch (Exception e) {
            log.error("getRankingGenre Error");
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    public List<GetRankingRes> getRankingThisMonth(int partyLocation, int partyGenre, int limit) {
        try {
            return mapper.getRankingThisMonth(partyLocation, partyGenre, limit);
        } catch(Exception e) {
            log.error("getRankingThisMonth Error");
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    public List<GetRankingRes> getRankingLastMonth(int partyLocation, int partyGenre, int limit) {
        try {
            return mapper.getRankingLastMonth(partyLocation, partyGenre, limit);
        } catch(Exception e) {
            log.error("getRankingLastMonth Error");
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }
}
