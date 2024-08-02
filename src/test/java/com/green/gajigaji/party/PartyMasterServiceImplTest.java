package com.green.gajigaji.party;

import com.green.gajigaji.common.model.CustomFileUtils;
import com.green.gajigaji.party.model.PostPartyReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@Import({ PartyServiceImpl.class })
class PartyMasterServiceImplTest {

    @Value("${file.directory}") String uploadPath;
    @MockBean PartyMapper mapper;
    @MockBean CustomFileUtils customFileUtils;
    @Autowired PartyService service;

    @Test
    @DisplayName("모임 생성 + 모임장 등록")
    void postParty() throws Exception {
        PostPartyReq p = new PostPartyReq
                (1,"축구 모임",1,0101,1
                ,1,1990,2010,20
                ,"축구하실분","나이 자기소개 필수"
                ,1,null);


        MultipartFile fm = new MockMultipartFile(
                "pic","a.jpg","image/jpg"
                ,new FileInputStream(String.format("%stest/a.jpg",uploadPath))
        );

        String randomFileNm1 = "a1b2.jpg";

        given(customFileUtils.makeRandomFileName(fm)).willReturn(randomFileNm1);
        p.setPartyPic(randomFileNm1);




    }

    @Test
    @DisplayName("지역 불러오기")
    void getPartyLocation() {
    }

    @Test
    @DisplayName("모임들 불러오기")
    void getParty() {
    }

    @Test
    @DisplayName("모임 하나 불러오기")
    void getPartyDetail() {
    }

    @Test
    @DisplayName("나의 모임들 불러오기(내가 모임장인건 제외)")
    void getPartyMine() {
    }

    @Test
    @DisplayName("내가 모임장인 모임들 불러오기")
    void getPartyLeader() {
    }

    @Test
    @DisplayName("모임 정보 수정")
    void updateParty() {
    }

    @Test
    @DisplayName("모임 생성 승인")
    void updatePartyAuthGb() {
    }

    @Test
    @DisplayName("모임 삭제(휴먼,복구 기능은 X)")
    void updatePartyForGb2() {
    }
}