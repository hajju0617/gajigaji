package com.green.gajigaji.partywish;


import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.partywish.model.PartyWishGetListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.green.gajigaji.common.GlobalConst.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/party/wish")
public class PartyWishController {
    private final PartyWishService service;

    @GetMapping
    @Operation(summary = "관심모임 찜" , description =
            "<strong > 관심 모임 찜 </strong> <p></p>" +
            "<p><strong> wishUserSeq</strong> : 유저 PK (long) </p>" +
            "<p><strong> wishPartySeq</strong> : 모임 PK (long) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  관심모임 추가 : 1 </p> " +
                            "<p>  관심모임 삭제 : 0 </p> "
    )
    public ResultDto<Integer> togglePartyWish(long wishPartySeq) {
            int result = service.togglePartyWish(wishPartySeq);
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.OK)
                    .code(SUCCESS)
                    .resultMsg(result == 0 ? "관심모임 삭제" : "관심모임 추가")
                    .resultData(result)
                    .build();

    }

    @GetMapping("/list")
    public ResultDto<List<PartyWishGetListRes>> partyWishGetList() {
            List<PartyWishGetListRes> result = service.partyWishGetList();
            return ResultDto.<List<PartyWishGetListRes>>builder()
                    .status(HttpStatus.OK)
                    .code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE)
                    .resultData(result)
                    .build();

    }
}
