package com.green.gajigaji.member;


import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.member.exception.MemberErrorCode;
import com.green.gajigaji.member.model.GetMemberRes;
import com.green.gajigaji.member.model.UpdateMemberReq;
import com.green.gajigaji.member.model.UpdateMemberRes;
import com.green.gajigaji.security.AuthenticationFacade;
import com.green.gajigaji.user.UserMapper;
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
    private final AuthenticationFacade authenticationFacade;
    private final UserMapper userMapper;
    private final CheckMapper checkMapper;
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
         * exception 부분 마우스 올리면 추가 주석 나옴. (('CRUD 약자' + '번호' + '메소드명') + 설명 있음) */

        // 멤버 정보들 return
        return ResultDto.resultDto(HttpStatus.OK,1, " 멤버들의 정보를 불러왔습니다.", mapper.getMember(memberPartySeq));
    }

    //모임의 멤버 한명의 정보 불러오기
    public ResultDto<GetMemberRes> getMemberDetail(Long memberPartySeq, Long memberUserSeq) {
        // 멤버 한명 정보들 return
        return ResultDto.resultDto(HttpStatus.OK,1, "멤버 한명의 정보를 불러왔습니다."
                , mapper.getMemberDetail(memberPartySeq, memberUserSeq));
    }

    //멤버 역할 수정(3차때 사용)
    public ResultDto<UpdateMemberRes> updateMember(Long memberPartySeq, UpdateMemberReq p) {
        // 멤버 역할 수정
        p.setMemberPartySeq(memberPartySeq);
        mapper.updateMember(p);
        return ResultDto.resultDto(HttpStatus.OK,1, "멤버 권한을 수정하였습니다.");
    }

    //멤버 탈퇴 상태로 변경 (모임장이 멤버 권한을 수정함)
    public ResultDto<UpdateMemberRes> updateMemberGb(long memberSeq, long memberPartySeq) {
        //JWT 예외처리
        long userPk = authenticationFacade.getLoginUserId();
        if(userMapper.userExists(userPk) == 0) {
            throw new CustomException(MemberErrorCode.NOT_FOUND_USER);
        }
        // 모임장인지 체크
        Integer roleChk = checkMapper.checkMemberRole(memberPartySeq, userPk);
        if(roleChk == null) {
            throw new CustomException(MemberErrorCode.NOT_JOINED_USER); // 해당 모임에 없는 유저입니다 체크
        } else if(roleChk == 1) {
            if(checkMapper.checkMemberUserSeqByMemberSeq(memberSeq) == userPk) {
                throw new CustomException(MemberErrorCode.NOT_ALLOWED_TO_PRESIDENT);
            }
            mapper.updateMemberGb(memberSeq);
            return ResultDto.resultDto(HttpStatus.OK,1, "해당 유저를 탈퇴 상태로 변경하였습니다.");
        } else if(roleChk == 3) {
            throw new CustomException(MemberErrorCode.ALREADY_LEFT_USER); // 이미 탈퇴된 유저입니다.
        } else {
            if(checkMapper.checkMemberUserSeqByMemberSeq(memberSeq) != userPk) {
                throw new CustomException(MemberErrorCode.NOT_MATCHED_USER); // 접속한 유저와 탈퇴하려는 유저의 pk가 다름
            }
            mapper.updateMemberGb(memberSeq);
            return ResultDto.resultDto(HttpStatus.OK,1, "모임에서 탈퇴하였습니다.");
        }
    }
}
