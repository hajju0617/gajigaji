package com.green.gajigaji.party.exception;

import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.common.myexception.MsgException;
import com.green.gajigaji.common.myexception.MsgExceptionNull;
import com.green.gajigaji.common.myexception.NullReqValue;
import com.green.gajigaji.common.myexception.ReturnDto;
import com.green.gajigaji.party.model.PostPartyReq;
import com.green.gajigaji.party.model.UpdatePartyReq;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Order(1)
@RestControllerAdvice(basePackages = "com.green.gajigaji.party")
public class PartyExceptionHandler {
    private final CheckMapper mapper;

    /** C(postParty) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param partyPic 사진을 미첨부.
     * @param p 이미 존재하는 모임 이름, 존재하지 않는 유저 <br><br>
     *
     *   1.사진을 첨부안함 : <br>
     *    - MsgExceptionNull("2,사진은 필수입니다."); <br><br>
     *   2.이미 존재하는 모임 이름 : <br>
     *    - MsgException("2,이미 존재하는 모임명칭입니다."); <br><br>
     *   3-1.존재하지 않는 유저 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   3-2.존재하지 않는 유저 : <br>
     *    없는 PK값 : MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     */
    public void exception(@Nullable MultipartFile partyPic, PostPartyReq p) {
        exception(partyPic);
        exception(p.getPartyName(), p.getPartySeq());
        exceptionUser(p.getUserSeq());
    }

    /** U1(updateParty) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param partyPic 사진을 미첨부.
     * @param p 이미 존재하는 모임 이름, 존재하지 않는 모임 <br>
     *          , 존재하지 않는 유저 , 권한이 없는 유저 <br><br>
     *   1.사진을 첨부안함 : <br>
     *    - MsgExceptionNull("2,사진은 필수입니다."); <br><br>
     *   2.이미 존재하는 모임 이름 : <br>
     *    - MsgException("2,이미 존재하는 모임명칭입니다."); <br><br>
     *   3-1.존재하지 않는 모임 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   3-2.존재하지 않는 모임 : <br>
     *    없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     *   4-1.존재하지 않는 유저 : <br>
     *    null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *   4-2.존재하지 않는 유저 : <br>
     *    없는 PK값 - MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     *   5.모임장이 아님(권한자 X) : <br>
     *    !모임장 - MsgException("2,권한이 없는 유저입니다."); <br><br>
     */
    public void exception(@Nullable MultipartFile partyPic, UpdatePartyReq p) {
        exception(partyPic);
        exception(p.getPartyName(), p.getPartySeq());
        exceptionLeader(p.getPartySeq(), p.getUserSeq());
    }

    /** U2(updateParty), U3(updatePartyAuthGb1) D(updatePartyAuthGb2) <br>
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
    public void exception(Long partySeq, Long userSeq) {
        exceptionLeader(partySeq, userSeq);
    }

    /** R3(getPartyDetail) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param partySeq 존재하지 않는 모임임 <br><br>
     *  1-1.존재하지 않는 모임 : <br>
     *   null - ResultDto.resultDto (HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *  1-2.존재하지 않는 모임 : <br>
     *   없는 PK - MsgExceptionNull("2,존재하지 않는 모임입니다."); <br><br>
     */
    public void exception(Long partySeq) {
        if (partySeq == null || partySeq == 0) {throw new NullReqValue();}
        if (mapper.checkPartySeq(partySeq) == 0) {
            throw new MsgExceptionNull("2,존재하지 않는 모임입니다.");}
    }

    /** R4(getPartyMine), R5(getPartyLeader) <br>
     * 커스텀 에러가 발생하는 경우는 아래와 같다.
     * @param userSeq 존재하지 않는 유저임 <br><br>
     *  1-1.존재하지 않는 유저 : <br>
     *  null - ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "정보를 제대로 입력해주세요."); <br><br>
     *  1-2.존재하지 않는 유저 : <br>
     *  없는 PK -  MsgExceptionNull("2,존재하지 않는 유저입니다."); <br><br>
     */
    public void exceptionUser(Long userSeq) {
        if (userSeq == null || userSeq == 0) {throw new NullReqValue();}
        if (mapper.checkUserSeq(userSeq) == 0) {throw new MsgExceptionNull("2,존재하지 않는 유저입니다.");}
    }
    public void exceptionLeader(Long partySeq, Long userSeq) {
        exception(partySeq);
        exceptionUser(userSeq);
        if (mapper.checkPartyLeader(partySeq, userSeq) != 1){
            throw new MsgException("2,권한이 없는 유저입니다.");
        }
    }
    public void exception(@Nullable MultipartFile partyPic) {
        if (partyPic == null || partyPic.isEmpty()) {throw new MsgExceptionNull("2,사진은 필수입니다.");}
    }
    public void exception(String partyName, Long partySeq) {
        if (partyName == null) {throw new NullReqValue();}
        if (mapper.checkPartyName(partyName, partySeq) != 0){
            throw new MsgException("2,이미 존재하는 모임명칭입니다.");
        }
    }

    //-2.메세지 출력용(커스텀)
    //MsgException('내용1','내용2')의 값을
    //ResultDto.resultDto(HttpStatus.BAD_REQUEST,'내용1','내용2') 으로 반환해줌
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
    //MsgExceptionNull('내용1','내용2')의 값을
    //ResultDto.resultDto(HttpStatus.NOT_FOUND,'내용1','내용2') 으로 반환해줌
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
    //MsgException,MsgExceptionNull 사용하기 전에 썻던 메소드이다.
    //ResultDto.resultDto(HttpStatus.OK,'내용1','내용2')을 사용하고 싶을경우 사용하면된다.
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
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "(party) 처리할 수 없는 요청입니다.");
    }
    //3.널포인트
    @ExceptionHandler(NullPointerException.class)
    public ResultDto<String> handleNullPointerException(NullPointerException ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.BAD_REQUEST,2, "(party) 정보가 없습니다.");
    }
    //4.Exception
    @ExceptionHandler(Exception.class)
    public ResultDto<String> handleException(Exception ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.INTERNAL_SERVER_ERROR,2, "(party) 서버에러입니다.");
    }

}