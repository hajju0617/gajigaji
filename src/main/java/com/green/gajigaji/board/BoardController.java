package com.green.gajigaji.board;


import com.green.gajigaji.board.model.*;
import com.green.gajigaji.common.GlobalConst;
import com.green.gajigaji.common.model.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/board")

public class BoardController {
    private final BoardService service;

    @PostMapping
    @Operation(summary = "게시글 등록")

    public ResultDto<BoardPostRes> postBoard(@RequestPart List<MultipartFile> pics, @RequestPart BoardPostReq p) {
        BoardPostRes result = service.postBoard(pics, p);

        return ResultDto.<BoardPostRes>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg("정상처리 되었습니다")
                .resultData(result)
                .build();
    }
    @DeleteMapping
    @Operation(summary = "게시글 삭제") /*,description =
            "<strong> 커뮤니티 게시글 삭제   </strong><p></p>" +
                    "<p><strong> boardSeq  </strong> : 게시글 PK (long) </p>" +
                    "<p><strong> boardMemberSeq </strong> : 게시글 유저 PK (long) </p>"
    ) */

    public ResultDto<Integer> deleteBoard(@RequestBody BoardDeleteReq p ) {
        int result = service.deleteBoard(p);

        return ResultDto.<Integer>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg("정상처리 되었습니다")
                .resultData(result)
                .build();
    }

    @PatchMapping
    @Operation(summary = "게시글 수정" )
    /*,description =
            "<strong> 커뮤니티 게시글 수정    </strong><p></p>" +
                    "<p><strong> boardSeq  </strong> : 게시글 PK (long) </p>" +
                    "<p><strong> boardMemberSeq </strong> : 게시글 유저 PK (long) </p>" +
                    "<p><strong> boardTitle   </strong> : 게시글 제목 (strong) </p>" +
                    "<p><strong> nowFileNames </strong> : 수정시 존재할 사진 (String) </p>" +
                    "<p><strong> deleteFileNames </strong> : 수정시 삭제할 사진 (string) </p>"
    )*/

    public ResultDto<BoardPatchReq> patchBoard(@RequestParam List<MultipartFile> newPics, @RequestBody BoardPatchReq p) {
        boolean result = service.boardPatch(newPics, p);
        return ResultDto.<BoardPatchReq>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg("정상처리 되었습니다")
                .resultData(p)
                .build();
    }
    @GetMapping
    @Operation(summary = "게시글 조회" )
    public ResultDto<BoardGetPage> getBoard(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "boardPartySeq", defaultValue = "0") int boardPartySeq) {
        BoardGetReq data = new BoardGetReq(0, page, GlobalConst.PAGING_SIZE);
        data.setBoardPartySeq(boardPartySeq);
        BoardGetPage list = service.getBoardDetail(data);
        return ResultDto.<BoardGetPage>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg("정상처리 되었습니다")
                .resultData(list)
                .build();
    }
    @GetMapping("/{boardSeq}")
    @Operation(summary = "게시글 상세 조회")

    public ResultDto<BoardGetRes> getBoardDetail(@ParameterObject @ModelAttribute BoardGetReq data) {
        BoardGetRes board = service.getBoard(data);
        return ResultDto.<BoardGetRes>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg("정상처리 되었습니다")
                .resultData(board)
                .build();
    }
}
