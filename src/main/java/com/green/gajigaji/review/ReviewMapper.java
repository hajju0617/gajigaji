package com.green.gajigaji.review;

import com.green.gajigaji.review.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    int postReview(PostReviewReq p);
    int postReviewPics(PostReviewPicDto p);
    int deleteReview(long reviewSeq);
    int deleteReviewPics(long reviewSeq);
    List<GetReviewAllRes> getReviewAll(GetReviewAllReq p);
    List<GetReviewUserRes> getReviewUser(GetReviewUserReq p);
    List<GetReviewPartyRes> getReviewParty(GetReviewPartyReq p);
    long getTotalElements(int search, String searchData, long userSeq);
    long getTotalElements(int search, String searchData);
    long getTotalElementsByPartySeq(long partySeq);
    void patchReview(PatchReviewReq p);
    List<GetReviewPicDto> getPic(long reviewSeq);
    List<String> getPicFiles(long reviewSeq);
    void deleteReviewFavs(long reviewSeq);
    int deleteReviewFav(GetReviewFavToggleReq p);
    int insertReviewFav(GetReviewFavToggleReq p);

    //JWT
    List<Long> checkAuthReviewSeq(long userSeq);
    List<Long> checkAuthPlmemberSeq(long userSeq);
    int checkAuthUserSeq(long userSeq);
}