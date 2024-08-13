package com.green.gajigaji.review;

import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.CustomFileUtils;
import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.review.exception.ReviewErrorCode;
import com.green.gajigaji.review.model.*;
import com.green.gajigaji.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper mapper;
    private final CheckMapper chkMapper;
    private final CustomFileUtils customFileUtils;
    private final AuthenticationFacade authenticationFacade;
    private String path = "review/";

    @Transactional
    public PostReviewRes postReview(List<MultipartFile> pics, PostReviewReq p){
        //JWT 예외처리
        long userPk = authenticationFacade.getLoginUserId();
        List<Long> list = mapper.checkAuthPlmemberSeq(userPk); // 해당 유저가 참가한 일정의 일정참가자 PK들을 list에 담아서 체크
        if(!list.contains(p.getReviewPlmemberSeq())) {
            log.error("해당 userPk가 참가한 일정들의 일정참가자PK list에 등록하려는 유저의 일정참가자PK가 없거나, 토큰이 넘어오지 않았음.");
            throw new CustomException(ReviewErrorCode.NOT_FOUND_MESSAGE);
        }
        if( chkMapper.checkPostedReview(p.getReviewPlanSeq(), p.getReviewPlmemberSeq()) != null) {
            throw new CustomException(ReviewErrorCode.DUPLICATED_REVIEW);
        }
        try {
            int result = mapper.postReview(p);

            if(result == 0) {
                log.error("Review Post Result == 0");
                throw new CustomException(ReviewErrorCode.REVIEW_POST_ERROR);
            }
            if (pics == null) {
                return PostReviewRes.builder()
                        .reviewSeq(p.getReviewSeq())
                        .build();
            }

            PostReviewPicDto ppic = postPics(p.getReviewSeq(), pics, path + p.getReviewSeq());

            return PostReviewRes.builder()
                    .reviewSeq(ppic.getReviewSeq())
                    .pics(ppic.getFileNames())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    @Transactional
    public List<GetReviewPicDto> patchReview(List<MultipartFile> pics, PatchReviewReq p){
        try {
            //JWT 예외처리
            long userPk = authenticationFacade.getLoginUserId();
            List<Long> list = mapper.checkAuthReviewSeq(userPk); // 해당 유저가 적은 리뷰의 PK들을 list에 담아서 체크
            if(!list.contains(p.getReviewSeq())) {
                log.error("해당 userPk가 적은 리뷰들의 PK list에 수정하려는 리뷰의 PK가 없거나, 토큰이 넘어오지 않았음.");
                throw new CustomException(ReviewErrorCode.NOT_FOUND_MESSAGE);
            }

            if(chkMapper.checkReview(p.getReviewSeq()) == null) { // 리뷰 있는지 체크
                throw new CustomException(ReviewErrorCode.NOT_FOUND_REVIEW);
            }
            if(chkMapper.checkReviewPics(p.getReviewSeq()) != null) { // 리뷰 사진 있으면 사진삭제+폴더삭제
                mapper.deleteReviewPics(p.getReviewSeq());
                customFileUtils.deleteFolder(path + p.getReviewSeq());
            }
            
            mapper.patchReview(p);

            if (pics == null) {
                return null;
            }

            PostReviewPicDto ppic = postPics(p.getReviewSeq(), pics, path + p.getReviewSeq());
            return mapper.getPic(ppic.getReviewSeq());
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Review Patch Error");
            throw new CustomException(ReviewErrorCode.REVIEW_PATCH_ERROR);
        }
    }

    @Transactional
    public int deleteReview(long reviewSeq) {
        try {
            //JWT 예외처리
            long userPk = authenticationFacade.getLoginUserId();
            List<Long> list = mapper.checkAuthReviewSeq(userPk); // 해당 유저가 적은 리뷰의 PK들을 list에 담아서 체크
            if(!list.contains(reviewSeq)) {
                log.error("해당 userPk가 적은 리뷰들의 PK list에 삭제하려는 리뷰의 PK가 없거나, 토큰이 넘어오지 않았음.");
                throw new CustomException(ReviewErrorCode.NOT_FOUND_MESSAGE);
            }

            if(chkMapper.checkReview(reviewSeq) == null) {
                throw new CustomException(ReviewErrorCode.NOT_FOUND_REVIEW);
            }
            mapper.deleteReviewPics(reviewSeq);
            mapper.deleteReviewFavs(reviewSeq);
            int result = mapper.deleteReview(reviewSeq);
            if(result == 0) {
                log.error("Review Delete Result == 0");
                throw new CustomException(ReviewErrorCode.REVIEW_DELETE_ERROR);
            }
            return result;
        }catch (Exception e) {
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    public GetReviewAllPageRes getReviewAll(GetReviewAllReq p) {
        try {
            List<GetReviewAllRes> res = mapper.getReviewAll(p);

            for (GetReviewAllRes idx : res) {
                List<String> pics = mapper.getPicFiles(idx.getReviewSeq());
                idx.setPics(pics);
            }

            GetReviewAllPageRes pageRes = new GetReviewAllPageRes(
                    mapper.getTotalElements(p.getSearch(), p.getSearchData())
                    , p.getSize()
                    , res
            );
            return pageRes;
        }catch (Exception e) {
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    public GetReviewUserPageRes getReviewUser(GetReviewUserReq p) {
        try {
            //JWT 예외처리
            long userPk = authenticationFacade.getLoginUserId();
            Integer authChk = mapper.checkAuthUserSeq(userPk); // 유저 PK 존재하는지 체크, 성공 결과값 : 1
            if(authChk == null) {
                log.error("유저 PK가 존재하지 않거나, 토큰이 넘어오지 않음.");
                throw new CustomException(ReviewErrorCode.NOT_FOUND_MESSAGE);
            }
            p.setUserSeq(userPk);

            List<GetReviewUserRes> res = mapper.getReviewUser(p);

            for (GetReviewUserRes idx : res) {
                List<String> pics = mapper.getPicFiles(idx.getReviewSeq());
                idx.setPics(pics);
            }

            GetReviewUserPageRes pageRes = new GetReviewUserPageRes(
                    mapper.getTotalElementsByUser(p.getSearch(), p.getSearchData(), p.getUserSeq())
                    , p.getSize()
                    , res
            );
            return pageRes;
        } catch (Exception e) {
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }
    
    // 모임pk별 조회, 비회원도 가능한 모임 상세조회라서 JWT필요없음
    public List<GetReviewPartyRes> getPartyReview(long partySeq, int limit) {

        try {
            List<GetReviewPartyRes> list = mapper.getReviewParty(partySeq, limit);

            for (GetReviewPartyRes idx : list) {
                List<String> pics = mapper.getPicFiles(idx.getReviewSeq());
                idx.setPics(pics);
            }

            return list;
        } catch (Exception e) {
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    public PostReviewPicDto postPics(long reviewSeq, List<MultipartFile> pics, String path) {
        try {
            PostReviewPicDto ppic = PostReviewPicDto.builder()
                    .reviewSeq(reviewSeq)
                    .build();

            customFileUtils.makeFolders(path);

            for (MultipartFile pic : pics) {
                String fileName = customFileUtils.makeRandomFileName(pic);
                String target = String.format("%s/%s", path, fileName);
                customFileUtils.transferTo(pic, target);
                ppic.getFileNames().add(fileName);
            }
            Integer result = mapper.postReviewPics(ppic);

            if (result == null) {
                log.error("Pics Post Result == 0");
                throw new CustomException(ReviewErrorCode.REVIEW_PICS_POST_ERROR);
            }

            return ppic;
        } catch (Exception e) {
            log.error("Pics Post Error");
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }

    public int toggleReviewFav(GetReviewFavToggleReq p) {
        try {
            //JWT 예외처리
            long userPk = authenticationFacade.getLoginUserId();
            Integer authChk = mapper.checkAuthUserSeq(userPk); // 유저 PK 존재하는지 체크, 성공 결과값 : 1
            if(authChk == null) {
                log.error("유저 PK가 존재하지 않거나, 토큰이 넘어오지 않음.");
                throw new CustomException(ReviewErrorCode.NOT_FOUND_MESSAGE);
            }
            p.setReviewFavUserSeq(userPk);

            if(chkMapper.checkReview(p.getReviewFavReviewSeq()) == null) {
                throw new CustomException(ReviewErrorCode.NOT_FOUND_REVIEW);
            }

            int result = mapper.deleteReviewFav(p);

            if (result == 1) {
                return 1;
            } else {
                result = mapper.insertReviewFav(p);
                return result == 1 ? 2 : 3;
            }
        } catch (Exception e) {
            throw new CustomException(ReviewErrorCode.UNIDENTIFIED_ERROR);
        }
    }
}
