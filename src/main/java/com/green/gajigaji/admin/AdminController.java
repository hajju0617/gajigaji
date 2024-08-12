package com.green.gajigaji.admin;

import com.green.gajigaji.admin.model.UpdatePartyGb;
import com.green.gajigaji.common.model.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.green.gajigaji.common.GlobalConst.SUCCESS;
import static com.green.gajigaji.common.GlobalConst.SUCCESS_MESSAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "admin", description = "admin CRUD")
public class AdminController {

    private final AdminService service;


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/auth")
    @Operation(summary = "모임 생성 승인" , description =
            "<strong>  사이트 관리자가 모임 생성을 승인함 (현재는 모임장이 승인) <strong><p></p>" +
                    "<p><strong> partySeq      </strong> : 모임PK (long) </p> " +
                    "<p><strong> num      </strong> : 상태값 (int) { 1 : 기본 값, 2 : 승인, 3 : 반려, 4 : 삭제 </p> " +
                    "<p><strong> userEmail      </strong> : 모임장 email (String) </p> " +
                    "<p><strong> text      </strong> : 메일에 보낼 내용 (String) </p> ")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<Integer> updatePartyAuthGb(@RequestBody UpdatePartyGb p) {
        int result = service.updatePartyAuthGb(p);

        return ResultDto.<Integer>builder()
                .status(HttpStatus.OK)
                .code(SUCCESS)
                .resultMsg(SUCCESS_MESSAGE)
                .resultData(result)
                .build();
    }


}
