package com.green.gajigaji.member;


import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.member.model.GetMemberRes;
import com.green.gajigaji.member.model.UpdateMemberReq;
import com.green.gajigaji.member.model.UpdateMemberRes;
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
@RequestMapping("api/member")
@Tag(name = "member", description = "member CRUD")
public class MemberController {
    private final MemberService service;

    @GetMapping("/{partySeq}")
    @Operation(summary = "멤버들 정보 불러오기", description =
            "<strong> 입력한 모임의 모든 멤버의 정보를 불러옵니다</strong><p></p>" +
                    "<p><strong> memberPartySeq      </strong> : 모임 PK (long) </p>" )
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<List<GetMemberRes>> getMember(@PathVariable("partySeq") Long memberPartySeq) {
        return service.getMember(memberPartySeq);
    }

    @GetMapping("/detail/{partySeq}")
    @Operation(summary = "멤버 한명 정보 불러오기", description =
            "<strong> 입력한 모임의 지정한 멤버의 정보를 불러옵니다</strong><p></p>" +
                    "<p><strong> memberPartySeq      </strong> : 모임 PK (long) </p>"+
                    "<p><strong> memberUserSeq      </strong> : 멤버 PK (long) </p>" )
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<GetMemberRes> getMemberDetail(@PathVariable("partySeq") Long memberPartySeq,
                                                 @RequestParam(name = "memberUserSeq") Long memberUserSeq) {
        return service.getMemberDetail(memberPartySeq, memberUserSeq);
    }

    @PatchMapping("/{partySeq}")
    @Operation(summary = "멤버 역할 수정(*미사용* 멤버 세분화가 필요해요.)", description =
            "<strong> 멤버의 역할을 수정합니다. (memberRole 1 = 모임장, 2= 모임원)</strong><p></p>" +
                    "<p><strong> memberPartySeq      </strong> : 모임 PK (long) </p>" +
                    "<p><strong> memberUserSeq      </strong> : 수정할 멤버 PK (long) </p>" +
                    "<p><strong> memberRole      </strong> : 멤버역할 (int) </p>" )
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<UpdateMemberRes> updateMember(@PathVariable("partySeq") Long memberPartySeq,
                                                   @RequestBody UpdateMemberReq p){
        return service.updateMember(memberPartySeq, p);
    }

    @PatchMapping("/leave")
    @Operation(summary = "멤버 탈퇴 기능(모임장, 회원 모두 사용 가능)" , description =
                "<strong> 멤버를 모임에서 차단합니다. (수정시 모임에서 비활성화 됨.)</strong><p></p>" +
                        "<p><strong> memberSeq      </strong> : 멤버 PK (long) </p>" +
                        "<p><strong> memberPartySeq      </strong> : 모임 PK (long) </p>" )
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<UpdateMemberRes> updateMemberGb(@RequestBody long memberSeq
    , @RequestBody long memberPartySeq) {
        return service.updateMemberGb(memberSeq, memberPartySeq);
    }
}
