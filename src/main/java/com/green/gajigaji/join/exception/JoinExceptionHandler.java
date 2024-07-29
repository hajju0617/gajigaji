package com.green.gajigaji.join.exception;

import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.common.myexception.MsgException;
import com.green.gajigaji.common.myexception.MsgExceptionNull;
import com.green.gajigaji.common.myexception.NullReqValue;
import com.green.gajigaji.common.myexception.ReturnDto;
import com.green.gajigaji.join.JoinMapper;
import com.green.gajigaji.join.model.PostJoinReq;
import com.green.gajigaji.join.model.UpdateJoinGbReq;
import com.green.gajigaji.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@Order(1)
@RestControllerAdvice(basePackages = "com.green.gajigaji.join")
public class JoinExceptionHandler {
    private final CheckMapper mapper;
    private final JoinMapper joinMapper;

    /** C(postJoin) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param partySeq 존재하지 않는 모임
     * @param p 존재하지 않는 유저, 권한이 없는 유저(멤버장은 신청 불가) <br>
     *          , 이미 신청한 경우 <br><br>
     *   1-1.존재하지 않는 모임 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   1-2.존재하지 않는 모임 : <br>
     *    없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     *   2-1.존재하지 않는 유저 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   2-2.존재하지 않는 유저 : <br>
     *    없는 PK값 - MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     *   3.모임장이 자신의 모임에 신청 : <br>
     *    - MsgException("2,권한이 없는 유저입니다."); <br><br>
     *   4.이미 신청한 모임 : <br>
     *    - MsgException("2,이미 신청한 모임입니다."); <br><br>
     */
    public void exception(Long partySeq,PostJoinReq p) {
        exceptionParty(partySeq);
        exceptionUser(p.getJoinUserSeq());
        if (mapper.checkPartyLeader(partySeq, p.getJoinUserSeq()) != 0){
            throw new MsgException("2,권한이 없는 유저입니다.");}
        if (mapper.checkMemberForPartySeqAndUserSeq(partySeq,p.getJoinUserSeq()) != 0) {
            joinMapper.deleteJoin(partySeq,p.getJoinUserSeq());}
        if (mapper.checkJoinApplicationOfUser(partySeq,p.getJoinUserSeq()) != 0) {
            throw new MsgException("2,이미 신청한 모임입니다.");}
    }


    /** U2(updateJoinGb)
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param partySeq 존재하지 않는 모임, 모임 인원수가 최대인 경우
     * @param p 존재하지 않는 유저, 존재하지 않는 유저의 신청서<br>
     *          , 권한이 없는 유저 <br><br>
     *   1-1.존재하지 않는 모임 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   1-2.존재하지 않는 모임 : <br>
     *    없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     *   2-1.존재하지 않는 유저 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   2-2.존재하지 않는 유저 : <br>
     *    없는 PK값 - MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     *   3.존재하지 않는 신청서 : <br>
     *    - MsgExceptionNull("2,존재하지 않는 신청서입니다."); <br><br>
     *   4.모임장이 아닌 유저(권한자 X) : <br>
     *    !모임장 - MsgException("2,권한이 없는 유저입니다."); <br><br>
     *   5.모임인원수가 최대인 경우 : <br>
     *    - MsgException("2,승인이 실패되었습니다. (모임인원수가 최대입니다)");
     */
    public void exception(Long partySeq, UpdateJoinGbReq p) {
        exception(partySeq, p.getJoinUserSeq());
        exceptionLeader(partySeq, p.getLeaderUserSeq());
        if (p.getJoinGb() == 1 && mapper.checkPartyNowMem(partySeq) == 1){
            throw new MsgException("2,승인이 실패되었습니다. (모임인원수가 최대입니다)");
        }
    }

    /** R2(getJoinDetail) U1(updateJoin) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param partySeq 존재하지 않는 모임
     * @param userSeq 존재하지 않는 유저, 존재하지 않는 유저의 신청서 <br><br>
     *   1-1.존재하지 않는 모임 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   1-2.존재하지 않는 모임 : <br>
     *    없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     *   2-1.존재하지 않는 유저 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   2-2.존재하지 않는 유저 : <br>
     *    없는 PK값 - MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     *   3.존재하지 않는 신청서 : <br>
     *    - MsgExceptionNull("2,존재하지 않는 신청서입니다."); <br><br>
     */
    public void exception(Long partySeq,Long userSeq) {
        exceptionParty(partySeq);
        exceptionUser(userSeq);
        if (mapper.checkJoinApplicationOfUser(partySeq, userSeq) == 0) {throw new MsgExceptionNull("2,존재하지 않는 신청서입니다.");}
    }

    public void exceptionParty(Long partySeq) {
        if (partySeq == null || partySeq == 0) {throw new NullReqValue();}
        if (mapper.checkPartySeq(partySeq) == 0) {throw new MsgExceptionNull("2,존재하지 않는 모임입니다.");}
    }
    public void exceptionUser(Long userSeq) {
        if (userSeq == null || userSeq == 0) {throw new NullReqValue();}
        if (mapper.checkUserSeq(userSeq) == 0) {throw new MsgExceptionNull("2,존재하지 않는 유저입니다.");}
    }

    /** R1(getJoin) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param partySeq 존재하지 않는 모임
     * @param userSeq 존재하지 않는 유저, 권한이 없는 유저 <br> <br>
     *  1-1.존재하지 않는 모임 : <br>
     *   null - ResultDto.resultDto (HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *  1-2.존재하지 않는 모임 : <br>
     *   없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     *  2-1.존재하지 않는 유저 : <br>
     *   null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *  2-2.존재하지 않는 유저 : <br>
     *   없는 PK - MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     *  3.모임장이 아닌 유저(권한자 X) : <br>
     *   !모임장 - MsgException("2,권한이 없는 유저입니다."); <br><br>
     */
    public void exceptionLeader(Long partySeq, Long userSeq) {
        exceptionParty(partySeq);
        exceptionUser(userSeq);
        if (mapper.checkPartyLeader(partySeq, userSeq) != 1){
            throw new MsgException("2,권한이 없는 유저입니다.");}
    }


    //-2.메세지 출력용(커스텀)
    @ExceptionHandler(MsgException.class)
    public ResultDto<String> handleMsgException(MsgException ex) {
        ex.printStackTrace();
        String msg = ex.getMessage();
        String[] parts = msg.split(",", 2);
        int code = Integer.parseInt(parts[0]);
        String resultMsg = parts[1];
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,code, resultMsg);
    }
    //-1.메세지 출력용(커스텀)
    @ExceptionHandler(MsgExceptionNull.class)
    public ResultDto<String> handleMsgExceptionNull(MsgExceptionNull ex) {
        ex.printStackTrace();
        String msg = ex.getMessage();
        String[] parts = msg.split(",", 2);
        int code = Integer.parseInt(parts[0]);
        String resultMsg = parts[1];
        return ResultDto.resultDto(HttpStatus.NOT_FOUND,code, resultMsg);
    }

    //0.메세지 출력용(커스텀)
    @ExceptionHandler(ReturnDto.class)
    public ResultDto<String> ReturnDto(ReturnDto ex) {
        ex.printStackTrace();
        String msg = ex.getMessage();
        String[] parts = msg.split(",", 2);
        int code = Integer.parseInt(parts[0]);
        String resultMsg = parts[1];
        return ResultDto.resultDto(HttpStatus.OK,code, resultMsg);
    }

    //1.Req 널(커스텀)
    @ExceptionHandler(NullReqValue.class)
    public ResultDto<String> handleNullReqValue(NullReqValue ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요.");
    }

    //2.런타임
    @ExceptionHandler(RuntimeException.class)
    public ResultDto<String> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "(join) 처리할 수 없는 요청입니다.");
    }
    //3.널포인트
    @ExceptionHandler(NullPointerException.class)
    public ResultDto<String> handleNullPointerException(NullPointerException ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "(join) 정보가 없습니다.");
    }
    //4.Exception
    @ExceptionHandler(Exception.class)
    public ResultDto<String> handleException(Exception ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "(join) 서버에러입니다.");
    }



}