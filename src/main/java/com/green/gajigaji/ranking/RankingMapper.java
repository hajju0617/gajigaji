package com.green.gajigaji.ranking;

import com.green.gajigaji.ranking.model.GetRankingRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RankingMapper {
    List<GetRankingRes> getRankingTotal(int limit);
    List<GetRankingRes> getRankingLocation(int partyLocation, int limit);
    List<GetRankingRes> getRankingGenre(int partyGenre, int limit);
    List<GetRankingRes> getRankingThisMonth(int partyLocation, int partyGenre, int limit);
    List<GetRankingRes> getRankingLastMonth(int partyLocation, int partyGenre, int limit);
}
