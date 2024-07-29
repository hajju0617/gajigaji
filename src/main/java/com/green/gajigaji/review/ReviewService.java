package com.green.gajigaji.review;

import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.CustomFileUtils;
import com.green.gajigaji.review.exception.CustomException;
import com.green.gajigaji.review.exception.ReviewErrorCode;
import com.green.gajigaji.review.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private String path = "review/";

    @Transactional
    public PostReviewRes postReview(List<MultipartFile> pics, PostReviewReq p) throws Exception{
        if(chkMapper.checkPostedReview(p.getReviewPlanSeq(), p.getReviewPlmemberSeq()) == 1) {
            System.out.println("들어왔음");
            throw new CustomException(ReviewErrorCode.DUPLICATED_REVIEW);
        }

        try {
            mapper.postReview(p);

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
            throw new Exception();
        }
    }

    @Transactional
    public List<GetReviewPicDto> patchReview(List<MultipartFile> pics, PatchReviewReq p) throws Exception{
        mapper.deleteReviewPics(p.getReviewSeq());
        mapper.patchReview(p);
        customFileUtils.deleteFolder(path + p.getReviewSeq());

        if(pics == null) {
            return null;
        }

        PostReviewPicDto ppic = postPics(p.getReviewSeq(), pics, path+p.getReviewSeq());
        return mapper.getPic(ppic.getReviewSeq());
    }

    @Transactional
    public int deleteReview(long reviewSeq) {
        mapper.deleteReviewPics(reviewSeq);
        mapper.deleteReviewFavs(reviewSeq);
        return mapper.deleteReview(reviewSeq);
    }

    public GetReviewAllPageRes getReviewAll(GetReviewAllReq p) {
        List<GetReviewAllRes> res = mapper.getReviewAll(p);

        for(GetReviewAllRes idx : res) {
            List<String> pics = mapper.getPicFiles(idx.getReviewSeq());
            idx.setPics(pics);
        }

        GetReviewAllPageRes pageRes = new GetReviewAllPageRes(
                mapper.getTotalElements(p.getSearch(),p.getSearchData(), 0)
                , p.getSize()
                , res
        );
        return pageRes;
    }

    public GetReviewUserPageRes getReviewUser(GetReviewUserReq p) {
        List<GetReviewUserRes> res = mapper.getReviewUser(p);

        for(GetReviewUserRes idx : res) {
            List<String> pics = mapper.getPicFiles(idx.getReviewSeq());
            idx.setPics(pics);
        }

        GetReviewUserPageRes pageRes = new GetReviewUserPageRes(
                mapper.getTotalElements(p.getSearch(),p.getSearchData(), p.getUserSeq())
                , p.getSize()
                , res
        );
        return pageRes;
    }

    public PostReviewPicDto postPics(long reviewSeq, List<MultipartFile> pics, String path) throws Exception {
        PostReviewPicDto ppic = PostReviewPicDto.builder()
                .reviewSeq(reviewSeq)
                .build();

        customFileUtils.makeFolders(path);
        for(MultipartFile pic : pics) {
            String fileName = customFileUtils.makeRandomFileName(pic);
            String target = String.format("%s/%s", path, fileName);
            customFileUtils.transferTo(pic, target);
            ppic.getFileNames().add(fileName);
        }
        mapper.postReviewPics(ppic);

        return ppic;
    }

    public int toggleReviewFav(GetReviewFavToggleReq p) {
        int result = mapper.deleteReviewFav(p);

        if (result == 1) {
            return 1;
        } else {
            result = mapper.insertReviewFav(p);
            return result == 1 ? 2 : 3;
        }
    }
}
