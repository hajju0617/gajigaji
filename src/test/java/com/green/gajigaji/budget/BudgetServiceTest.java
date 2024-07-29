package com.green.gajigaji.budget;

import com.green.gajigaji.budget.model.PostBudgetReq;
import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.CustomFileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@Import(BudgetService.class)
@TestPropertySource(
        properties = {
                "file.directory=D:/Yerim/project_2nd_tdd"
        })
class BudgetServiceTest {

    @Value("${file.directory}") String uploadPath;
    @MockBean
    BudgetMapper mapper;
    @MockBean CustomFileUtils customFileUtils;
    @MockBean CheckMapper checkMapper;
    @Autowired
    BudgetService service;

    @Test
    void postBudget() throws Exception {
        PostBudgetReq p1 = new PostBudgetReq();
        p1.setBudgetPartySeq(1L);
        p1.setBudgetGb(1);
        p1.setBudgetAmount(10000);
        p1.setBudgetDt("24-07-23");
        MultipartFile fm1 = new MockMultipartFile(
                "pic", "4745ff96-e651-49b1-895a-784703f567f2.png", "image/png",
                new FileInputStream("D:/Yerim/greengramtdd/user/1/4745ff96-e651-49b1-895a-784703f567f2.png")
        );
        given(mapper.postBudget(p1)).willReturn(1);

        PostBudgetReq p2 = new PostBudgetReq();
        p2.setBudgetPartySeq(2L);
        p2.setBudgetGb(2);
        p2.setBudgetAmount(20000);
        p2.setBudgetDt("24-08-23");
        MultipartFile fm2 = new MockMultipartFile(
                "pic", "4745ff96-e651-49b1-895a-784703f567f2.png", "image/png",
                new FileInputStream("D:/Yerim/greengramtdd/user/1/4745ff96-e651-49b1-895a-784703f567f2.png")
        );
        given(mapper.postBudget(p2)).willReturn(2);



    }

    @Test
    void patchBudget() {
    }

    @Test
    void getBudget() {
    }

    @Test
    void getBudgetPic() {
    }

    @Test
    void deleteBudget() {
    }

    @Test
    void getBudgetMember() {
    }

    @Test
    void getBudgetMonthly() {
    }
}