package com.green.gajigaji.join;


import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.common.myexception.ReturnDto;
import com.green.gajigaji.join.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class JoinService {
    private final JoinMapper mapper;
    private final CheckMapper checkMapper;
    /** "JoinExceptionHandler"는 "GlobalExceptionHandler"보다 순위가 높음. @Order(1) == 1순위임.
     * "JoinExceptionHandler"의 범위는 (basePackages = "com.green.gajigaji.member")이다.
     * "JoinExceptionHandler"는 (MsgException,MsgExceptionNull,ReturnDto,NullReqValue,RuntimeException,NullPointerException,Exception)의 '에러' 발생시
     *  어노테이션 @ExceptionHandler('에러'.class)으로 사용자가 '에러'에 대응하게 설정한 값들을 리턴해줌.
     * check.exception('값')은 잘못된 '값'에 대해서 사용자가 설정한 커스텀 에러를 터트림(커스텀 에러 : MsgException,MsgExceptionNull,ReturnDto,NullReqValue)
     * 커스텀 에러중 "MsgException","MsgExceptionNull","ReturnDto"는 내가 원하는 메세지를 출력하기 위한 에러임.
     *  ex1) MsgException("2,정보를 제대로 입력해주세요.") HttpStatus.BAD_REQUEST 반환하기 위해서 사용.
     *       -> ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요.");
     *  ex2) MsgExceptionNull("2,사진은 필수입니다.")는 HttpStatus.NOT_FOUND 반환하기 위해서 사용.
     *       -> ResultDto.resultDto(HttpStatus.NOT_FOUND,2, "사진은 필수입니다.");
     */


    //모임 신청서 작성 (모임 가입을 위한 신청서 작성)
    public ResultDto<Integer> postJoin(Long joinPartySeq, PostJoinReq p) {
        /** 일부러 에러를 터트려서 원하는 값을 return 함. (설명을 리턴 값 번호 + 에러가 발생한 이유로 정리함.)
         * exception 부분 마우스 올리면 추가 주석 나옴. (('CRUD 약자' + '번호' + '메소드명') + 설명 있음)
         */
        // (특이사항) 해당 "check.exception"는 "joinMapper.deleteJoin(partySeq,p.getJoinUserSeq())"가 포함되어 있음.
        // 차단당한 유저가 재가입을 희망할 경우 DB 무결성을 위해 해당 메소드가 필요함.

        // 모임 신청서 작성
        p.setJoinPartySeq(joinPartySeq);
        mapper.postJoin(p);
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 가입신청을 하였습니다");
    }


    //모임 신청서들 불러오기 (모임장이 신청된 신청서들 확인용)
    public ResultDto<List<GetJoinRes>> getJoin(Long joinPartySeq, Long leaderUserSeq) {
        // 모임 신청서들 정보 return
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 신청서들을 불러옵니다.", mapper.getJoin(joinPartySeq));
    }


    //모임 신청서 하나 불러오기 (신청한 유저가 자신의 신청서 확인용)
    public ResultDto<GetJoinRes> getJoinDetail(Long joinPartySeq, Long joinUserSeq) {
        // 자신의 신청서 정보 return
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 신청서를 불러옵니다.", mapper.getJoinDetail(joinPartySeq, joinUserSeq));
    }


    //모임 신청서 내용 수정하기 (신청한 유저가 자신의 신청서의 내용을 수정함)
    public ResultDto<Integer> updateJoin(Long joinPartySeq, UpdateJoinReq p) {
        p.setJoinPartySeq(joinPartySeq);
        // 모임 신청서 수정
        mapper.updateJoin(p);
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 신청서를 수정합니다.");
    }

    //모임 신청서 승인 + 멤버 등록 or 차단된 멤버 권한 수정
    //(모임장이 신청서 승인시 신청서 정보와 멤버 정보가 추가 or 수정됨)
    //흐름도 : 에러 확인 -> 신청서 거절함? -> 이전에 추방당한 멤버임? -> 새로운 멤버 등록자임?
    public ResultDto<Integer> updateJoinGb(Long joinPartySeq, UpdateJoinGbReq p) {
        p.setJoinPartySeq(joinPartySeq);

        // 신청서 수정 (getJoinGb가 1:승인, 2:거절)
        mapper.updateJoinGb(p);

        // 모임장이 신청서를 거절함.
        if(p.getJoinGb()==2){return ResultDto.resultDto(HttpStatus.OK,1, " 신청서를 거절하였습니다.");}

        // if = 추방당한 유저인지 확인, 차단 당한 멤버 정보를 수정함(DB 참조값), 신청서를 승인함.
        if(checkMapper.checkMemberForPartySeqAndUserSeq(joinPartySeq, p.getJoinUserSeq()) == 1){
            mapper.updateSuspendedMember(joinPartySeq,p.getJoinUserSeq());
            throw new ReturnDto("1,신청서를 승인하였습니다. (1: 멤버등록)");}

        // 신청서를 작성한 유저를 멤버로 등록함.
        mapper.postMember(joinPartySeq,p.getJoinUserSeq());
        return ResultDto.resultDto(HttpStatus.OK,1, " 신청서를 승인하였습니다.");
    }


    //모임 신청서 삭제 (신청한 유저가 자신의 신청서를 삭제함)
    public ResultDto<Integer> deleteJoin(Long joinPartySeq, Long joinUserSeq) {
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 신청서를 삭제합니다.", mapper.deleteJoin(joinPartySeq, joinUserSeq));
    }

    /** (!!) GetMyJoinRes 빈값 채워야해요 */
    public ResultDto<List<GetMyJoinRes>> getMyJoin(long userSeq){
        if(checkMapper.checkUserSeq(userSeq) == 0){
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, "존재하지 않는 유저입니다.");
        }
        return ResultDto.resultDto(HttpStatus.OK,1, " 자신의 모임 신청서를 모두 불러옵니다.", mapper.getMyJoin(userSeq));
    }

}
