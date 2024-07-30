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
    @Operation(summary = "게시글 삭제")
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
    @Operation(summary = "게시글 수정")
    public ResultDto<BoardPatchReq> patchBoard(@RequestParam List<MultipartFile> newPics, @RequestBody BoardPatchReq p) {
        BoardPatchReq result = service.boardPatch(newPics, p);
        return ResultDto.<BoardPatchReq>builder()
                .status(HttpStatus.OK)
                .code(1)
                .resultMsg("정상처리 되었습니다")
                .resultData(result)
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
