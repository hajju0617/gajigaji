package com.green.gajigaji.review;

import com.green.gajigaji.review.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    void postReview(PostReviewReq p);
    void postReviewPics(PostReviewPicDto p);
    int deleteReview(long reviewSeq);
    void deleteReviewPics(long reviewSeq);
    List<GetReviewAllRes> getReviewAll(GetReviewAllReq p);
    List<GetReviewUserRes> getReviewUser(GetReviewUserReq p);
    long getTotalElements(int search, String searchData, long userSeq);
    void patchReview(PatchReviewReq p);
    List<GetReviewPicDto> getPic(long reviewSeq);
    List<String> getPicFiles(long reviewSeq);
    void deleteReviewFavs(long reviewSeq);
    int deleteReviewFav(GetReviewFavToggleReq p);
    int insertReviewFav(GetReviewFavToggleReq p);
}