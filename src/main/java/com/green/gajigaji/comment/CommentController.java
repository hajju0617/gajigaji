package com.green.gajigaji.comment;


import com.green.gajigaji.comment.model.CommentGetPage;
import com.green.gajigaji.comment.model.CommentDeleteReq;
import com.green.gajigaji.comment.model.CommentGetReq;
import com.green.gajigaji.comment.model.CommentPatchReq;
import com.green.gajigaji.comment.model.CommentPostReq;
import com.green.gajigaji.common.model.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.green.gajigaji.common.GlobalConst.COMMENT_PAGING_SIZE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/board/comment")
public class CommentController {
    private final CommentService service;

    @PostMapping
    @Operation(summary = "댓글 등록" , description =
    "<strong> 커뮤니티 댓글 등록    </strong><p></p>" +
    "<p><strong> commentBoardSeq  </strong> : 게시글PK (long) </p>" +
    "<p><strong> commentMemberSeq </strong> : 댓글 유저PK (long) </p>" +
    "<p><strong> commentContents  </strong> : 댓글 내용 (strong) </p>"
    )
    public ResultDto<Long> postComment(@RequestBody CommentPostReq p) {
        long result = service.postBoardComment(p);
        return ResultDto.<Long>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg(result == 1 ? "정상처리" : "실패")
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "댓글 삭제" ,description =
    "<strong> 커뮤니티 댓글 삭제 </strong><p></p>" +
    "<p><strong> commentSeq </strong> : 댓글PK (long) </p>" +
    "<p><strong> commentMemberSeq </strong> : 댓글 유저PK (long) </p>")
    public ResultDto<Integer> deleteComment(@RequestBody CommentDeleteReq p) {
        int result = service.deleteBoardComment(p);
        return ResultDto.<Integer>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg(result == 1 ? "정상처리" : "실패")
                .resultData(result)
                .build();
    }

    @PatchMapping
    @Operation(summary = "댓글 수정", description =
    "<strong> 커뮤니티 댓글 수정 </strong><p></p>" +
    "<p><strong> commentSeq </strong> : 댓글PK (long) </p>" +
    "<p><strong> commentMemberSeq </strong> : 댓글 유저PK (long) </p>" +
    "<p><strong> commentContents </strong> : 댓글 내용 (String) </p>")
    public ResultDto<Integer> patchComment(@ModelAttribute @ParameterObject CommentPatchReq p) {
        int result = service.patchBoardComment(p);
        return ResultDto.<Integer>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg(result == 1 ? "정상처리" : "실패")
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "댓글 조회", description =
    "<strong> 커뮤니티 댓글 조회(페이징처리) </strong><p></p>" +
    "<p><strong> boardSeq </strong> : 게시글PK (long) </p>" +
    "<p><strong> page </strong> : 페이지 입력 (Integer) </p>")
    public ResultDto<CommentGetPage> getComment(@RequestParam(name = "boardSeq") long boardSeq, Integer page, Integer size) {
      CommentGetReq p = new CommentGetReq(page, size,boardSeq);

      CommentGetPage list = service.getBoardComment(p);

        return ResultDto.<CommentGetPage>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg("정상처리 되었습니다")
                .resultData(list)
                .build();
    }

}
