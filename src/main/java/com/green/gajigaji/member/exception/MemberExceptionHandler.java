package com.green.gajigaji.member.exception;

import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.common.myexception.MsgException;
import com.green.gajigaji.common.myexception.MsgExceptionNull;
import com.green.gajigaji.common.myexception.NullReqValue;
import com.green.gajigaji.common.myexception.ReturnDto;
import com.green.gajigaji.member.model.UpdateMemberReq;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RequiredArgsConstructor
@Order(1)
@RestControllerAdvice(basePackages = "com.green.project2nd.member")
public class MemberExceptionHandler {
    private final CheckMapper mapper;

    /** R2(getMemberDetail) U2(updateMemberGb) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param memberPartySeq 존재하지 않는 모임
     * @param memberUserSeq 존재하지 않는 유저, 존재하지 않는 멤버 <br><br>
     *   1-1.존재하지 않는 모임 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   1-2.존재하지 않는 모임 : <br>
     *    없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     *   2-1.존재하지 않는 유저 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   2-2.존재하지 않는 유저 : <br>
     *    없는 PK값 - MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     *   3. 존재하지 않는 멤버 : <br>
     *    - MsgExceptionNull("2,존재하지 않는 멤버입니다."); <br><br>
     */
    public void exception(Long memberPartySeq, Long memberUserSeq) {
        exceptionParty(memberPartySeq);
        exceptionUser(memberUserSeq);
        if (mapper.checkMemberForPartySeqAndUserSeq(memberPartySeq,memberUserSeq) == 0){throw new MsgExceptionNull("2,존재하지 않는 멤버입니다.");}
    }

    /** U1(updateMember) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param memberPartySeq 존재하지 않는 모임
     * @param p 존재하지 않는 유저, 존재하지 않는 멤버 <br><br>
     *   1-1.존재하지 않는 모임 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   1-2.존재하지 않는 모임 : <br>
     *    없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     *   2-1.존재하지 않는 유저 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   2-2.존재하지 않는 유저 : <br>
     *    없는 PK값 - MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     *   3. 존재하지 않는 멤버 : <br>
     *    - MsgExceptionNull("2,존재하지 않는 멤버입니다."); <br><br>
     */
    public void exception(Long memberPartySeq, UpdateMemberReq p) {
        exception(memberPartySeq, p.getMemberUserSeq());
    }

    // U3

    /** U2(updateMemberGb) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param memberPartySeq 존재하지 않는 모임
     * @param memberLeaderUserSeq 존재하지 않는 유저, 존재하지 않는 멤버
     *                            , 권한이 없는 유저 <br><br>
     *   1-1.존재하지 않는 모임 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   1-2.존재하지 않는 모임 : <br>
     *    없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     *   2-1.존재하지 않는 유저 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   2-2.존재하지 않는 유저 : <br>
     *    없는 PK값 - MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     *   3. 존재하지 않는 멤버 : <br>
     *    - MsgExceptionNull("2,존재하지 않는 멤버입니다."); <br><br>
     *   4. 권한이 없는 유저 : <br>
     *    - MsgExceptionNull("2,권한이 없는 유저입니다."); <br><br>
     */
    public void exceptionLeader(Long memberPartySeq, Long memberLeaderUserSeq) {
        exception(memberPartySeq,memberLeaderUserSeq);
        if (mapper.checkPartyLeader(memberPartySeq, memberLeaderUserSeq) != 1){
            throw new MsgException("2,권한이 없는 유저입니다.");
        }
    }

    /** R1(getMember) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param memberPartySeq 존재하지 않는 모임 <br><br>
     *   1-1.존재하지 않는 모임 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   1-2.존재하지 않는 모임 : <br>
     *    없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     */
    public void exceptionParty(Long memberPartySeq) {
        if (memberPartySeq == null || memberPartySeq == 0) {throw new NullReqValue();}
        if (mapper.checkPartySeq(memberPartySeq) == 0) {throw new MsgExceptionNull("2,존재하지 않는 모임입니다.");}
    }

    public void exceptionUser(Long memberUserSeq) {
        if (memberUserSeq == null || memberUserSeq == 0) {throw new NullReqValue();}
        if (mapper.checkUserSeq(memberUserSeq) == 0) {throw new MsgExceptionNull("2,존재하지 않는 유저입니다.");}
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
    public ResultDto<String> handleMsgException2(MsgExceptionNull ex) {
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
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "(member) 처리할 수 없는 요청입니다.");
    }
    //3.널포인트
    @ExceptionHandler(NullPointerException.class)
    public ResultDto<String> handleNullPointerException(NullPointerException ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "(member) 정보가 없습니다.");
    }
    //4.Exception
    @ExceptionHandler(Exception.class)
    public ResultDto<String> handleException(Exception ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "(member) 서버에러입니다.");
    }


}