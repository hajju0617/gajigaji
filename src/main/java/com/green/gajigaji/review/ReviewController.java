package com.green.gajigaji.review;

import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.review.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import static com.green.gajigaji.common.GlobalConst.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Tag(name = "Review (리뷰 관리)", description = "리뷰 CRUD")
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    @Operation(summary = "리뷰 등록", description =
    "<strong> 유저 리뷰 등록 (PostMan으로 테스트)</strong><p></p>" +
    "<p><strong> reviewPlanSeq      </strong> : 일정 PK (long) </p>" +
    "<p><strong> reviewPlmemberSeq  </strong> : 일정 참가자 PK (long) </p>" +
    "<p><strong> reviewContents     </strong> : 내용 (String) </p>" +
    "<p><strong> reviewRating       </strong> : 별점 (int) </p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (유저PK, 사진 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<PostReviewRes> postReview(@RequestPart(value = "pics", required = false) List<MultipartFile> pics
            , @RequestPart PostReviewReq p){
            PostReviewRes result = service.postReview(pics, p);
            return ResultDto.resultDto(HttpStatus.OK, SUCCESS,"리뷰 등록 완료" ,result);
    }

    @DeleteMapping
    @Operation(summary = "리뷰 삭제", description =
            "<strong> 본인이 적은 리뷰 삭제 </strong><p></p>" +
                    "<p><strong> reviewSeq </strong> : 리뷰 PK (long) </p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (DB에서 영향을 받은 행 갯수 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<Integer> deleteReview(@RequestParam(name = "reviewSeq", required = true) long reviewSeq) {
        int result = service.deleteReview(reviewSeq);
        return ResultDto.resultDto(HttpStatus.OK,SUCCESS,"삭제 완료(result = 영향받은 행 수)", result);
    }

    @GetMapping
    @Operation(summary = "리뷰 조회", description =
           "<strong> 전체 리뷰 조회 </strong><p></p>" +
                   "<p><strong> search </strong> : 검색어 구분값 (int) 1:전체 2:모임명 3:모임장명 4: 작성자명 (디폴트값 : 1)</p>" +
                   "<p><strong> searchData </strong> : 검색어 (String) (NULL 허용)" +
                   "<p><strong> page   </strong> : 페이지 번호 (Integer) (NULL 허용, 디폴트값 : 1)</p>" +
                   "<p><strong> size   </strong> : 페이지별 게시글 수 (Integer) (NULL 허용, 디폴트값 : 10)</p>")
    @ApiResponse(
           description =
                   "<p> ResponseCode 응답 코드 </p>" +
                           "<p> 1 : 성공 (사진,리뷰내용 List 형식으로 리턴)</p>" +
                           "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<GetReviewAllPageRes> getReviewAll(
                @RequestParam(name = "search", defaultValue = "1") Integer search
               , @Nullable @RequestParam(name = "searchData") String searchData
               , @Nullable @RequestParam(name = "page") Integer page
               , @Nullable @RequestParam(name = "size") Integer size
    ) {

       if(searchData == null) { searchData = ""; }
       if(page == null || page < 0) { page = 0; }
       if(size == null || size < 0) { size = 0; }

       GetReviewAllReq p = new GetReviewAllReq(page, size, search, searchData);
       GetReviewAllPageRes result = service.getReviewAll(p);

       return ResultDto.resultDto(HttpStatus.OK,SUCCESS, "리뷰 조회 완료", result);}

    @GetMapping("/user")
    @Operation(summary = "내가 적은 리뷰 검색", description =
           "<strong> 마이페이지 내가 적은 리뷰 </strong><p></p>" +
                   "<p><strong> userSeq   </strong> : 유저 PK (Integer) </p>" +
                   "<p><strong> search </strong> : 검색어 구분값 (int) 1:전체 2:모임명 3:모임장명 4: 작성자명</p>" +
                   "<p><strong> searchData </strong> : 검색어 (String) (NULL 허용)" +
                   "<p><strong> page   </strong> : 페이지 번호 (Integer) (NULL 허용, 디폴트값 : 1)</p>" +
                   "<p><strong> size   </strong> : 페이지별 게시글 수 (Integer) (NULL 허용, 디폴트값 : 10)</p>")
    @ApiResponse(
           description =
                   "<p> ResponseCode 응답 코드 </p>" +
                           "<p> 1 : 성공 (사진,리뷰내용 List 형식으로 리턴)</p>" +
                           "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<GetReviewUserPageRes> getReviewUser(@RequestParam(name = "search", defaultValue = "1") Integer search
           , @Nullable @RequestParam(name = "searchData") String searchData
           , @Nullable @RequestParam(name = "page") Integer page
           , @Nullable @RequestParam(name = "size") Integer size) {

        if(searchData == null) { searchData = ""; }
        if(page == null || page < 0) { page = 0; }
        if(size == null || size < 0) { size = 0; }

        GetReviewUserReq p = new GetReviewUserReq(page, size, search, searchData);
        GetReviewUserPageRes result = service.getReviewUser(p);
        return ResultDto.resultDto(HttpStatus.OK,SUCCESS, "리뷰 조회 완료", result);
    }


    @GetMapping("/party")
    @Operation(summary = "모임 별 리뷰 검색", description =
            "<strong> 모임 상세조회 리뷰 </strong><p></p>" +
                    "<p><strong> partySeq   </strong> : 모임 PK (Integer) </p>" +
                    "<p><strong> page   </strong> : 페이지 번호 (Integer) (NULL 허용, 디폴트값 : 1)</p>" +
                    "<p><strong> size   </strong> : 페이지별 게시글 수 (Integer) (NULL 허용, 디폴트값 : 10)</p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (사진,리뷰내용 List 형식으로 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<GetReviewPartyPageRes> getReviewParty(
            @RequestParam(name = "partySeq") long partySeq
            , @Nullable @RequestParam(name = "page") Integer page
            , @Nullable @RequestParam(name = "size") Integer size) {

        if(page == null || page < 0) { page = 0; }
        if(size == null || size < 0) { size = 0; }

        GetReviewPartyReq p = new GetReviewPartyReq(page, size, partySeq);
        GetReviewPartyPageRes result = service.getPartyReview(p);
        return ResultDto.resultDto(HttpStatus.OK,SUCCESS, "리뷰 조회 완료", result);
    }

    @PatchMapping
    @Operation(summary = "리뷰 수정", description =
            "<strong> 유저 리뷰 수정 (PostMan으로 테스트)</strong><p></p>" +
                    "<p><strong> reviewSeq          </strong> : 리뷰 PK (long) </p>" +
                    "<p><strong> reviewContents     </strong> : 내용 (String) </p>" +
                    "<p><strong> reviewRating       </strong> : 별점 (int) </p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (사진 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<List<GetReviewPicDto>> patchReview(@RequestPart(value = "pics", required = false) List<MultipartFile> pics
                                                         , @RequestPart PatchReviewReq p){
            List<GetReviewPicDto> result = service.patchReview(pics, p);
            return ResultDto.resultDto(HttpStatus.OK,SUCCESS, "리뷰 수정 완료", result);
    }

    @GetMapping("/fav")
    @Operation(summary = "도움돼요!", description =
            "<strong> 도움돼요 토글 (PostMan으로 테스트)</strong><p></p>" +
                    "<p><strong> reviewFavUserSeq   </strong> : 유저PK (long) </p>" +
                    "<p><strong> reviewFavReviewSeq </strong> : 리뷰PK (String) </p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공, ResultMsg</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<Integer> getReviewFavToggle(@ModelAttribute @ParameterObject GetReviewFavToggleReq p){
        int result = service.toggleReviewFav(p); //== 1 ? "취소" : "등록";
        return ResultDto.resultDto(HttpStatus.OK, SUCCESS, "Fav Toggle 처리 완료(1:취소, 2:등록)", result);
    }
}
