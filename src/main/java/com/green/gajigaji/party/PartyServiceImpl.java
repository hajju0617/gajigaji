package com.green.gajigaji.party;

import com.green.gajigaji.common.model.CustomFileUtils;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.party.model.*;
import com.green.gajigaji.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PartyServiceImpl implements PartyService {
    private final PartyMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final AuthenticationFacade authenticationFacade;

    //모임 신청 + 모임장 등록 (모임을 신청한 유저를 모임장으로 등록함)
    //throws Exceoption은 PartyExceptionHandler의 Exception이 받음. return : 서버에러 입니다.
    //p의 partyLocation 값은 아래 "getPartyLocation" 주석을 참고하시오.
    public ResultDto<PostPartyRes> postParty(@Nullable MultipartFile partyPic, PostPartyReq p) throws Exception {

        p.setUserSeq(authenticationFacade.getLoginUserId());
        /** 일부러 에러를 터트려서 원하는 값을 return 함. (설명을 리턴 값 번호 + 에러가 발생한 이유로 정리함.)
         * exception 부분 마우스 올리면 추가 주석 나옴. (('CRUD 약자' + '번호' + '메소드명') + 설명 있음)
         */
        // 랜덤 파일 이름 생성, p에 주입
        String saveFileName = customFileUtils.makeRandomFileName(partyPic);
        p.setPartyPic(saveFileName);

        // 모임 만듬
        mapper.postParty(p);
        String path = String.format("party/%d", p.getPartySeq());

        // 폴더 생성
        customFileUtils.makeFolders(path);
        String target = String.format("%s/%s", path, saveFileName);

        // 폴더에 파일 저장
        customFileUtils.transferTo(partyPic, target);

        // 모임장 만듬
        mapper.postMemberForPostParty(p);

        // 모임 PK, 랜덤 파일 이름 return
        return ResultDto.resultDto(HttpStatus.OK, 1, "모임을 생성하였습니다.", PostPartyRes.builder().partySeq(p.getPartySeq()).partyPic(p.getPartyPic()).build());
    }


    //지역 불러오기 (신청서 작성에 필요한 정보를 불러옴)
    //따로 에러처리 안함 (기본적인 에러는 "PartyExceptionHandler"에서 처리함.)
    //프론트와 약속한 값으로 주고 받기로함.
    //cdGb가 00인경우 cdSub 값에 맞는 (지역들의 값을 return)해줌.
    //"partyLocation"의 값은 cdSub는 앞에 2자리, cdSub는 뒷에 2자리에 값이 위치해서 들어가짐. (밑에 예시.)
    // ex1) cdSub == 01, cdSub == 02 -> partyLocation == 102 (젤 앞자리 0은 생략되고 DB에 들어가짐.)
    // ex2) cdSub == 11, cdSub == 02 -> partyLocation == 1102
    public ResultDto<List<GetPartyLocationRes>> getPartyLocation(int cdSub, int cdGb) {
        //지역 하나값을 return
        if (cdGb != 0) {
            return ResultDto.resultDto(HttpStatus.OK, 1, "지역을 불러왔습니다.", mapper.getPartyLocation(cdSub, cdGb));
        }
        //지역들의 값을 return
        return ResultDto.resultDto(HttpStatus.OK, 1, "지역들을 불러왔습니다.", mapper.getPartyLocationAll(cdSub));
    }


    //모임들 불러오기 (누구나 요청가능)
    //따로 에러처리 안함 (기본적인 에러는 "PartyExceptionHandler"에서 처리함.)
    public ResultDto<List<GetPartyRes>> getParty() {
        // 모임 정보들 return
        return ResultDto.resultDto(HttpStatus.OK, 1, "모임들을 불러왔습니다.", mapper.getParty());
    }


    //모임 하나 불러오기
    public ResultDto<GetPartyRes> getPartyDetail(Long partySeq) {
        // 모임 하나의 정보 return
        return ResultDto.resultDto(HttpStatus.OK, 1, "하나의 모임을 불러왔습니다.", mapper.getPartyDetail(partySeq));
    }


    //나의 모임들 불러오기(내가 모임장인 모임은 제외)
    public ResultDto<GetPartyRes2> getPartyMine(GetPartyReq2 p) {

        // 총 페이지, 모임 수 계산
        int TotalElements = mapper.getPartyMineCount(p.getUserSeq());
        int TotalPages = (int) Math.ceil((double) TotalElements / p.getSize());

        // return 할 모임 정보 정리
        List<GetPartyRes2List> list = mapper.getPartyMine(p);
        GetPartyRes2 res2 = new GetPartyRes2(TotalElements, TotalPages, list);
        return ResultDto.resultDto(HttpStatus.OK, 1, "나의 모임들을 불러왔습니다.(내가 모임장인 것은 제외)", res2);
    }


    //내가 모임장인 모임들 불러오기
    public ResultDto<GetPartyRes2> getPartyLeader(GetPartyReq2 p) {

        // 총 페이지, 모임 수 계산
        int TotalElements = mapper.getPartyLeaderCount(p.getUserSeq());
        int TotalPages = (int) Math.ceil((double) TotalElements / p.getSize());

        // return 할 모임 정보 정리
        List<GetPartyRes2List> list = mapper.getPartyLeader(p);
        GetPartyRes2 res2 = new GetPartyRes2(TotalElements, TotalPages, list);
        return ResultDto.resultDto(HttpStatus.OK, 1, "나의 모임들을 불러왔습니다.(내가 모임장인 것은 제외)", res2);
    }

    //모임 정보 수정
    public ResultDto<Integer> updateParty(@Nullable MultipartFile partyPic, UpdatePartyReq p) throws Exception {
        p.setUserSeq(authenticationFacade.getLoginUserId());

        if (partyPic != null && !partyPic.isEmpty()) {
            // 랜덤 이름, 파일 위치 설정
            String path = String.format("party/%d", p.getPartySeq());
            String saveFileName = customFileUtils.makeRandomFileName(partyPic);
            String target = String.format("%s/%s", path, saveFileName);

            // 파일 삭제, 폴더 만들기, 파일 생성
            customFileUtils.deleteFolder(path);
            customFileUtils.makeFolders(path);
            customFileUtils.transferTo(partyPic, target);

            // 모임 정보 수정
            p.setPartyPic(saveFileName);
        }
        return ResultDto.resultDto(HttpStatus.OK, 1, "모임을 수정하였습니다.", mapper.updateParty(p));
    }


//    /** (중요!!) userSeq <- 나중에 멤버장이 아닌, 관리자가 접근했을 때 승인해주게 해야함!
//     * 관리자를 어떻게 설정하는지에 따라서 코드 변경 하세용. */
//    //모임 생성 승인 (모임장이 자신의 모임을 승인함. 관리자 나오면 수정해야함.)
//    public ResultDto<Integer> updatePartyAuthGb1(Long partySeq) {
//        long userSeq = authenticationFacade.getLoginUserId();
//        check.exception(partySeq);
//
//        // 모임 상태 변경 (PartyAuthGb = 2)
//        mapper.updatePartyAuthGb1(partySeq, userSeq);
//        return ResultDto.resultDto(HttpStatus.OK,1,"모임 생성을 승인 하였습니다.");
//    }

    //모임 삭제 (활성화 -> 휴먼으로 모임의 상태만 변경함.)
    public ResultDto<Integer> updatePartyAuthGb2(Long partySeq, Long userSeq) {
        // 모임 상태 변경 (PartyAuthGb = 2)
        mapper.updatePartyAuthGb2(partySeq);
        return ResultDto.resultDto(HttpStatus.OK, 1, "모임을 삭제(휴먼) 하였습니다.");
    }
}