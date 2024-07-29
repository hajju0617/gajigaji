package com.green.gajigaji.join;


import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.join.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/join")
@Tag(name = "join", description = "join CRUD")
public class JoinController {
    private final JoinService service;

    //해당 모임에 신청한 신청서가 있는지 확인 필요함.(신청서 하나 가져오기 쓸 예정.)
    @PostMapping("/{joinPartySeq}")
    @Operation(summary = "신청서 등록" , description =
            "<strong> 모임 가입을 위한 신청서를 등록합니다. </strong><p></p>" +
                    "<p><strong> joinPartySeq      </strong> : 모임 PK (long) </p>" +
                    "<p><strong> joinUserSeq      </strong> : 유저 PK (long) </p>" +
                    "<p><strong> joinMsg      </strong> : 가입신청문 (String) </p>" )
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<Integer> postJoin(@PathVariable("joinPartySeq") Long joinPartySeq,
                                         @RequestBody PostJoinReq p) {
        return service.postJoin(joinPartySeq, p);
    }

    @GetMapping("/{joinPartySeq}")
    @Operation(summary = "신청서들 불러오기" , description =
            "<strong> 모임장이 신청서들을 확인합니다. </strong><p></p>" +
            "<p><strong> joinPartySeq      </strong> : 모임 PK (long) </p>" +
            "<p><strong> leaderUserSeq      </strong> : 모임장 유저 PK (long) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<List<GetJoinRes>> getJoin(@PathVariable("joinPartySeq") Long joinPartySeq,
                                               @RequestParam(name = "leaderUserSeq") Long leaderUserSeq) {
        return service.getJoin(joinPartySeq,leaderUserSeq);
    }
    @GetMapping("/detail/{joinPartySeq}")
    @Operation(summary = "신청서 하나 불러오기" , description =
            "<strong> 자신이 작성한 신청서를 불러옵니다. </strong><p></p>" +
                    "<p><strong> joinPartySeq      </strong> : 모임 PK (long) </p>" +
                    "<p><strong> joinUserSeq      </strong> : 유저 PK (long) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<GetJoinRes> getJoinDetail(@PathVariable("joinPartySeq") Long joinPartySeq,
                                               @RequestParam(name = "joinUserSeq") Long joinUserSeq) {
        return service.getJoinDetail(joinPartySeq, joinUserSeq);
    }

    @PatchMapping("/{joinPartySeq}")
    @Operation(summary = "신청서 수정" , description =
            "<strong> 자신이 작성한 신청서를 수정합니다. </strong><p></p>" +
                    "<p><strong> joinPartySeq      </strong> : 모임 PK (long) </p>" +
                    "<p><strong> joinUserSeq      </strong> : 유저 PK (long) </p>" +
                    "<p><strong> joinMsg      </strong> : 가입신청문 (String) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<Integer> updateJoin(@PathVariable("joinPartySeq") Long joinPartySeq,
                                               @RequestBody UpdateJoinReq p){
        return service.updateJoin(joinPartySeq, p);
    }
    @PatchMapping("gb/{joinPartySeq}")
    @Operation(summary = "신청서 승인(+멤버등록), 신청서 거절 " , description =
            "<strong> 신청서 상태를 수정합니다. (승인시 신청서 작성 유저를 멤버로 추가) </strong><p></p>" +
                    "<p><strong> joinPartySeq      </strong> : 모임 PK (long) </p>" +
                    "<p><strong> joinUserSeq      </strong> : 유저 PK (long) </p>" +
                    "<p><strong> leaderUserSeq      </strong> : 모임장 유저 PK (long) </p>" +
                    "<p><strong> joinGb      </strong> : 신청문 상태 (int) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<Integer> updateJoinGb(@PathVariable("joinPartySeq") Long joinPartySeq,
                                           @RequestBody UpdateJoinGbReq p){
        return service.updateJoinGb(joinPartySeq, p);
    }

    @DeleteMapping("/{joinPartySeq}")
    @Operation(summary = "신청서 삭제" , description =
            "<strong> 자신의 신청서를 삭제합니다.</strong><p></p>" +
                    "<p><strong> joinPartySeq      </strong> : 모임 PK (long) </p>" +
                    "<p><strong> joinUserSeq      </strong> : 유저 PK (long) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<Integer> deleteJoin(@PathVariable(name = "joinPartySeq") Long joinPartySeq,
                                         @RequestParam(name = "joinUserSeq") Long joinUserSeq){
        return service.deleteJoin(joinPartySeq, joinUserSeq);
    }

    @GetMapping
    @Operation(summary = "신청서 조회" , description =
            "<strong> 자신의 모임 신청서를 모두 불러옵니다. </strong><p></p>" +
                    "<p><strong> userSeq      </strong> : 유저 PK (long) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<List<GetMyJoinRes>> getMyJoin(long userSeq){
        return service.getMyJoin(userSeq);
    }
}
