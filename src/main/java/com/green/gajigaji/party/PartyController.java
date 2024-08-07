package com.green.gajigaji.party;


import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.party.model.*;
import com.green.gajigaji.security.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/party")
@Tag(name = "party", description = "party CRUD")
public class PartyController {
    private final PartyService service;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    @Operation(summary = "모임 생성+모임장 등록  (포스트맨 사용하세요)", description =
            "<strong>  새로운 모임을 생성함 <strong><p></p>" +
            "<strong>  신청한 유저를 해당 모임의 모임장으로 등록함 <strong><p></p>" +
                    "<p><strong> partyPic     </strong> : 모임사진 (MultipartFile) </p>"+
                    "<p><strong> userSeq      </strong> : 유저PK (long) </p>" +
                    "<p><strong> partyName       </strong> : 모임명 (String) </p>" +
                    "<p><strong> partyGenre      </strong> : 카테고리-분야 (int) </p>" +
                    "<p><strong> partyLocation  </strong> : 카테고리-지역 (int) </p>" +
                    "<p><strong> partyGender        </strong> : 카테고리-성별 (int) </p>" +
                    "<p><strong> partyMinAge     </strong> : 최소년도 (int) </p>" +
                    "<p><strong> partyMaxAge     </strong> : 최대년도 (int) </p>" +
                    "<p><strong> partyMaximum     </strong> : 최대인원수 (int) </p>" +
                    "<p><strong> partyJoinGb     </strong> : 멤버모집상태 (int) </p>" +
                    "<p><strong> partyIntro     </strong> : 모임소개 (String) </p>" +
                    "<p><strong> partyJoinForm     </strong> : 가입양식 (String) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<PostPartyRes> postParty(@RequestPart(required = false) MultipartFile partyPic
                                            , @RequestPart PostPartyReq p) throws Exception{
        return service.postParty(partyPic, p);
    }

    @GetMapping("location")
    @Operation(summary = "지역 불러오기", description =
            "<strong>  지역 불러오기 <strong><p></p>" +
                    "<strong>  지역 구에 0을 넣으면 해당 지역 시를 모두 보여줌 <strong><p></p>" +
                    "<p><strong> cdSub     </strong> : 지역 시 (int) </p>"+
                    "<p><strong> cdGb      </strong> : 지역 구 (int) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<List<GetPartyLocationRes>> getPartyLocation(@RequestParam(name = "cdSub") int cdSub
                                         ,@RequestParam(name = "cdGb") int cdGb) {
        return service.getPartyLocation(cdSub,cdGb);
    }
    @GetMapping("/party")
    @Operation(summary = "모임들 불러오기" , description = "모임 불러오기")
    public ResultDto<List<GetPartyRes>> getParty() {
        return service.getParty();
    }


    @GetMapping("/partyes")
    @Operation(summary = "모임들 불러오기/검색", description =
            "<strong> 모임들을 불러옵니다</strong><p></p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<GetPartyPage> getPartyes(
            @RequestParam(name = "search", defaultValue = "1") Integer search
            , @Nullable @RequestParam(name = "searchData") String searchData
            , @Nullable @RequestParam(name = "page") Integer page
            , @Nullable @RequestParam(name = "size") Integer size) {

        GetPartySearchReq p = new GetPartySearchReq(page, size, search, searchData);
        return service.getPartyes(p);
    }



    @GetMapping("/detail")
    @Operation(summary = "모임 하나 불러오기" , description = "모임 불러오기")
    public ResultDto<GetPartyRes> getPartyDetail(@RequestParam(name = "partySeq") Long partySeq) {
        return service.getPartyDetail(partySeq);
    }

    @GetMapping("/mine")
    @Operation(summary = "나의 모임들 불러오기(내가 모임장인 것은 제외)", description =
            "<strong> 나의 모임들 불러오기(내가 모임장인 것은 제외)</strong><p></p>" +
                    "<p><strong> userSeq      </strong> : 유저 PK (int) </p>" +
                    "<p><strong> page      </strong> : 조회 페이지 (int) (Null 허용) </p>" +
                    "<p><strong> size       </strong> : 조회 크기 (int) (Null 허용) </p>" )
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<GetPartyRes2> getPartyMine(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {

        long userPk = authenticationFacade.getLoginUserId();
        if(page <= 0){page = 1;}
        GetPartyReq2 req2 = new GetPartyReq2(page, size);
        req2.setUserSeq(userPk);
        return service.getPartyMine(req2);
    }

    @Operation(summary = "내가 모임장인 모임들 불러오기", description =
            "<strong> 내가 모임장인 모임들 불러오기</strong><p></p>" +
                    "<p><strong> userSeq      </strong> : 유저 PK (int) </p>" +
                    "<p><strong> page      </strong> : 조회 페이지 (int) (Null 허용) </p>" +
                    "<p><strong> size       </strong> : 조회 크기 (int) (Null 허용) </p>" )
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    @GetMapping("/leader")
    public ResultDto<GetPartyRes2> getPartyLeader(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        long userPk = authenticationFacade.getLoginUserId();
        GetPartyReq2 req2 = new GetPartyReq2(page, size);
        req2.setUserSeq(userPk);
        return service.getPartyLeader(req2);
    }



    @PatchMapping()
    @Operation(summary = "모임 수정 (포스트맨 사용하세요)" , description =
                "<strong>  기존 모임 정보를 수정함 <strong><p></p>" +
                        "<p><strong> partyPic     </strong> : 모임사진 (MultipartFile) </p>"+
                        "<p><strong> userSeq      </strong> : 유저PK (long) </p>" +
                        "<p><strong> partySeq      </strong> : 모임PK (long) </p>" +
                        "<p><strong> partyName       </strong> : 모임명 (String) </p>" +
                        "<p><strong> partyGenre      </strong> : 카테고리-분야 (int) </p>" +
                        "<p><strong> partyLocation  </strong> : 카테고리-지역 (int) </p>" +
                        "<p><strong> partyGender        </strong> : 카테고리-성별 (int) </p>" +
                        "<p><strong> partyMinAge     </strong> : 최소년도 (int) </p>" +
                        "<p><strong> partyMaxAge     </strong> : 최대년도 (int) </p>" +
                        "<p><strong> partyMaximum     </strong> : 최대인원수 (int) </p>" +
                        "<p><strong> partyJoinGb     </strong> : 멤버모집상태 (int) </p>" +
                        "<p><strong> partyIntro     </strong> : 모임소개 (String) </p>" +
                        "<p><strong> partyJoinForm     </strong> : 가입양식 (String) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")

    public ResultDto<UpdatePartyRes> updateParty(@RequestPart(required = false) MultipartFile partyPic
                                                , @RequestPart UpdatePartyReq p) throws Exception{
        return service.updateParty(partyPic, p);
    }

    //관리자가 모임 등록을 승인해주는 코드,관리자가 누군지 추가하고 권한줘야함. 현재는 모임장이 모임 생성 승인가능ㅋㅋ
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/authGb1")
    @Operation(summary = "모임 생성 승인" , description =
            "<strong>  사이트 관리자가 모임 생성을 승인함 (현재는 모임장이 승인) <strong><p></p>" +
            "<p><strong> partySeq      </strong> : 모임PK (long) </p>" +
            "<p><strong> userSeq      </strong> : 관리자 유저PK (long) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<Integer> updatePartyAuthGb1(@RequestParam(name = "partySeq") Long partySeq) {
        return service.updatePartyAuthGb1(partySeq);
    }

    @PatchMapping("/authGb2")
    @Operation(summary = "모임 삭제(휴먼,복구 기능은 X)" , description =
            "<strong>  모임 상태를 삭제로 변경함 (DB에 정보는 남아있음) <strong><p></p>" +
                    "<p><strong> partySeq      </strong> : 모임PK (long) </p>" +
                    "<p><strong> userSeq      </strong> : 모임장 유저PK (long) </p>")
    @ApiResponse(description =
            "<p> ResponseCode 응답 코드 </p>" +
                    "<p> 1 : 성공 </p>" +
                    "<p> 2 : 실패 </p>")
    public ResultDto<Integer> updatePartyAuthGb2(@RequestParam(name ="partySeq") Long partySeq){
        long userPk = authenticationFacade.getLoginUserId();
        return service.updatePartyAuthGb2(partySeq,userPk);
    }



}
