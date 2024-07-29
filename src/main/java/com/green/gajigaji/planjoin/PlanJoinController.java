package com.green.gajigaji.planjoin;

import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.planjoin.model.TogglePlanJoinReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/plan/join")
@Tag(name = "Plan Join (모임 일정 참가 관리)", description = "plan join CRUD")
public class PlanJoinController {
    private final PlanJoinService service;

    @PostMapping
    @Operation(summary = "모임 일정 참가 신청", description =
            "<strong> 등록되어 있는 일정에 참가 신청 (모임장 or 모임 멤버)<p></p>\n" +
            "<p><strong> plmemberPlanSeq</strong> : 모임 일정 마스터 PK 값 (long) </p>\n" +
            "<p><strong> memberSeq</strong> : 모임 멤버 PK 값 (long) </p>\n")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p> " +
                    "<p>  1 : 성공 </p> " +
                    "<p>  2 : 실패, ResultMsg </p> " +
                    "<p>  99 : 알 수 없는 오류 발생 실패</p> ")
    public ResultDto<Integer> postPlanJoin(@RequestBody TogglePlanJoinReq p) {
        return service.postPlanJoin(p);
    }

    @DeleteMapping
    @Operation(summary = "모임 일정 참가 신청 취소", description =
            "<strong> 등록되어 있는 일정에 참가 취소 신청 (모임장 or 모임 멤버)<p></p>\n" +
                    "<p><strong> plmemberPlanSeq</strong> : 모임 일정 마스터 PK 값 (long) </p>\n" +
                    "<p><strong> memberSeq</strong> : 모임 멤버 PK 값 (long) </p>\n")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p> " +
                    "<p>  1 : 성공 </p> " +
                    "<p>  2 : 실패, ResultMsg </p> " +
                    "<p>  99 : 알 수 없는 오류 발생 실패</p> ")
    public ResultDto<Integer> deletePlanJoin(@RequestBody TogglePlanJoinReq p) {
        return service.deletePlanJoin(p);
    }
}


