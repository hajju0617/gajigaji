package com.green.gajigaji.join;


import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.common.myexception.ReturnDto;
import com.green.gajigaji.join.exception.JoinErrorCode;
import com.green.gajigaji.join.model.*;
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
public class JoinService {
    private final JoinMapper mapper;
    private final CheckMapper checkMapper;
    private final AuthenticationFacade authenticationFacade;
    private final UserMapper userMapper;

    //모임 신청서 작성 (모임 가입을 위한 신청서 작성)
    public ResultDto<Integer> postJoin(Long joinPartySeq, PostJoinReq p) {
        /** 일부러 에러를 터트려서 원하는 값을 return 함. (설명을 리턴 값 번호 + 에러가 발생한 이유로 정리함.)
         * exception 부분 마우스 올리면 추가 주석 나옴. (('CRUD 약자' + '번호' + '메소드명') + 설명 있음)
         */
        // (특이사항) 해당 "check.exception"는 "joinMapper.deleteJoin(partySeq,p.getJoinUserSeq())"가 포함되어 있음.
        // 탈퇴 유저가 재가입을 희망할 경우 DB 무결성을 위해 해당 메소드가 필요함.

        // 모임 신청서 작성
        long userPk = authenticationFacade.getLoginUserId();
        if(userMapper.userExists(userPk) == 0) {
            throw new CustomException(JoinErrorCode.NOT_FOUND_USER);
        }
        //모임 맥시멈 인원수와 현재 인원 비교
        if(checkMapper.checkPartyNumberOfPeople(p.getJoinPartySeq()) >= checkMapper.checkPartyMaximumNumberOfPeople(p.getJoinPartySeq())) {
            throw new CustomException(JoinErrorCode.PARTY_MEMBER_FULL);
        }

        p.setJoinPartySeq(joinPartySeq);
        p.setJoinUserSeq(userPk);
        mapper.postJoin(p);
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 가입신청을 하였습니다");
    }


    //모임 신청서들 불러오기 (모임장이 신청된 신청서들 확인용)
    public ResultDto<List<GetJoinRes>> getJoin(Long joinPartySeq) {
        long userPk = authenticationFacade.getLoginUserId();
        if(userMapper.userExists(userPk) == 0) {
            throw new CustomException(JoinErrorCode.NOT_FOUND_USER);
        }

        Integer roleChk = checkMapper.checkMemberRole(joinPartySeq, userPk);
        if(roleChk == null || roleChk != 1) {
            throw new CustomException(JoinErrorCode.NOT_ALLOWED);
        }
        // 모임 신청서들 정보 return
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 신청서들을 불러옵니다.", mapper.getJoin(joinPartySeq));
    }


    //모임 신청서 하나 불러오기 (신청한 유저가 자신의 신청서 확인용)
    public ResultDto<GetJoinRes> getJoinDetail(Long joinPartySeq) {
        long userPk = authenticationFacade.getLoginUserId();
        if(userMapper.userExists(userPk) == 0) {
            throw new CustomException(JoinErrorCode.NOT_FOUND_USER);
        }
        // 자신의 신청서 정보 return
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 신청서를 불러옵니다.", mapper.getJoinDetail(joinPartySeq, userPk));
    }


    //모임 신청서 내용 수정하기 (신청한 유저가 자신의 신청서의 내용을 수정함)
    public ResultDto<Integer> updateJoin(Long joinPartySeq, UpdateJoinReq p) {
        long userPk = authenticationFacade.getLoginUserId();
        if(userMapper.userExists(userPk) == 0) {
            throw new CustomException(JoinErrorCode.NOT_FOUND_USER);
        }
        p.setJoinUserSeq(userPk);
        p.setJoinPartySeq(joinPartySeq);
        // 모임 신청서 수정
        mapper.updateJoin(p);
        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 신청서를 수정합니다.");
    }

    //모임 신청서 승인 + 멤버 등록 or 차단된 멤버 권한 수정
    //(모임장이 신청서 승인시 신청서 정보와 멤버 정보가 추가 or 수정됨)
    //흐름도 : 에러 확인 -> 신청서 거절함? -> 이전에 추방당한 멤버임? -> 새로운 멤버 등록자임?
    public ResultDto<Integer> updateJoinGb(Long joinPartySeq, UpdateJoinGbReq p) {
        long userPk = authenticationFacade.getLoginUserId();
        if(userMapper.userExists(userPk) == 0) {
            throw new CustomException(JoinErrorCode.NOT_FOUND_USER);
        }

        Integer roleChk = checkMapper.checkMemberRole(joinPartySeq, userPk);
        if(roleChk == null || roleChk != 1) {
            throw new CustomException(JoinErrorCode.NOT_ALLOWED);
        }
        p.setJoinPartySeq(joinPartySeq);

        //모임 맥시멈 인원수와 현재 인원 비교
        if(checkMapper.checkPartyNumberOfPeople(p.getJoinPartySeq()) >= checkMapper.checkPartyMaximumNumberOfPeople(p.getJoinPartySeq())) {
            throw new CustomException(JoinErrorCode.PARTY_MEMBER_FULL);
        }

        if(p.getJoinGb() < 1 || p.getJoinGb() > 2) {
            throw new CustomException(JoinErrorCode.BAD_REQUEST_JOIN_GB);
        }

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
    public ResultDto<Integer> deleteJoin(Long joinPartySeq) {
        long userPk = authenticationFacade.getLoginUserId();
        if(userMapper.userExists(userPk) == 0) {
            throw new CustomException(JoinErrorCode.NOT_FOUND_USER);
        }

        if(checkMapper.checkJoinApplicationForm(joinPartySeq, userPk) < 1) {
            throw new CustomException(JoinErrorCode.NOT_YOUR_APPLICATION_FORM);
        }

        return ResultDto.resultDto(HttpStatus.OK,1, " 모임 신청서를 삭제합니다.", mapper.deleteJoin(joinPartySeq, userPk));
    }

    /** (!!) GetMyJoinRes 빈값 채워야해요 */
    public ResultDto<List<GetMyJoinRes>> getMyJoin(){
        long userPk = authenticationFacade.getLoginUserId();
        if(userMapper.userExists(userPk) == 0) {
            throw new CustomException(JoinErrorCode.NOT_FOUND_USER);
        }

        return ResultDto.resultDto(HttpStatus.OK,1, " 자신의 모임 신청서를 모두 불러옵니다.", mapper.getMyJoin(userPk));
    }

}
