package com.green.gajigaji.member;


import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.member.exception.MemberExceptionHandler;
import com.green.gajigaji.member.model.GetMemberRes;
import com.green.gajigaji.member.model.UpdateMemberReq;
import com.green.gajigaji.member.model.UpdateMemberRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {
    private final MemberMapper mapper;
    private final MemberExceptionHandler check;
    /** "MemberExceptionHandler"는 "GlobalExceptionHandler"보다 순위가 높음. @Order(1) == 1순위임.
     * "MemberExceptionHandler"의 범위는 (basePackages = "com.green.gajigaji.join")이다.
     * "MemberExceptionHandler"는 (MsgException,MsgExceptionNull,ReturnDto,NullReqValue,RuntimeException,NullPointerException,Exception)의 '에러' 발생시
     *  어노테이션 @ExceptionHandler('에러'.class)으로 사용자가 '에러'에 대응하게 설정한 값들을 리턴해줌.
     * check.exception('값')은 잘못된 '값'에 대해서 사용자가 설정한 커스텀 에러를 터트림(커스텀 에러 : MsgException,MsgExceptionNull,ReturnDto,NullReqValue)
     * 커스텀 에러중 "MsgException","MsgExceptionNull","ReturnDto"는 내가 원하는 메세지를 출력하기 위한 에러임.
     *  ex1) MsgException("2,정보를 제대로 입력해주세요.") HttpStatus.BAD_REQUEST 반환하기 위해서 사용.
     *       -> ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요.");
     *  ex2) MsgExceptionNull("2,사진은 필수입니다.")는 HttpStatus.NOT_FOUND 반환하기 위해서 사용.
     *       -> ResultDto.resultDto(HttpStatus.NOT_FOUND,2, "사진은 필수입니다.");
     */

    //모임의 멤버들의 정보 불러오기
    public ResultDto<List<GetMemberRes>> getMember(Long memberPartySeq) {
        /** 일부러 에러를 터트려서 원하는 값을 return 함. (설명을 리턴 값 번호 + 에러가 발생한 이유로 정리함.)
         * exception 부분 마우스 올리면 추가 주석 나옴. (('CRUD 약자' + '번호' + '메소드명') + 설명 있음)
         */ check.exceptionParty(memberPartySeq);

        // 멤버 정보들 return
        return ResultDto.resultDto(HttpStatus.OK,1, " 멤버들의 정보를 불러왔습니다.", mapper.getMember(memberPartySeq));
    }

    //모임의 멤버 한명의 정보 불러오기
    public ResultDto<GetMemberRes> getMemberDetail(Long memberPartySeq, Long memberUserSeq) {
        check.exception(memberPartySeq, memberUserSeq);
        // 멤버 한명 정보들 return
        return ResultDto.resultDto(HttpStatus.OK,1, "멤버 한명의 정보를 불러왔습니다."
                , mapper.getMemberDetail(memberPartySeq, memberUserSeq));
    }

    //멤버 역할 수정(3차때 사용)
    public ResultDto<UpdateMemberRes> updateMember(Long memberPartySeq, UpdateMemberReq p) {
        check.exception(memberPartySeq, p);
        // 멤버 역할 수정
        p.setMemberPartySeq(memberPartySeq);
        mapper.updateMember(p);
        return ResultDto.resultDto(HttpStatus.OK,1, "멤버 권한을 수정하였습니다.");
    }

    //멤버 차단 (모임장이 멤버 권한을 수정함)
    public ResultDto<UpdateMemberRes> updateMemberGb(Long memberPartySeq, Long memberUserSeq, Long memberLeaderUserSeq) {
        //차단 당하는 사람 관련 에러 확인.
        check.exception(memberPartySeq, memberUserSeq);
        //차단 하는 사람 관련 에러 확인.
        check.exceptionLeader(memberPartySeq, memberLeaderUserSeq);

        // 멤버의 권한을 수정함 ( memberRole,Gb = 0 )
        mapper.updateMemberGb(memberPartySeq, memberUserSeq);
        return ResultDto.resultDto(HttpStatus.OK,1, "해당 멤버를 차단하였습니다.");
    }
}
