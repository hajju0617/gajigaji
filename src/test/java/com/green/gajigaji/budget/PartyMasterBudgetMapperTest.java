package com.green.gajigaji.budget;

import com.green.gajigaji.budget.model.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ActiveProfiles("tdd")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PartyMasterBudgetMapperTest {

    @Autowired
    private BudgetMapper mapper;

    final long budgetPartySeq = 1;
    final String month = "07";


    @Test
    void postBudget() {
        List<GetBudgetRes> list1 = mapper.getBudget(budgetPartySeq, month);
        int recordCountFirst = list1.size();

        PostBudgetReq p1 = new PostBudgetReq();
        p1.setBudgetPartySeq(budgetPartySeq);
        p1.setBudgetMemberSeq(1L);
        p1.setBudgetGb(1);
        p1.setBudgetAmount(10000);
        p1.setBudgetDt("24-" + month + "-01");
        p1.setBudgetPic("test.jpg");

        int affectedRows = mapper.postBudget(p1);
        assertEquals(1, affectedRows);

        List<GetBudgetRes> list2 = mapper.getBudget(budgetPartySeq, month);
        int recordCountAfterInsert = list2.size();

        assertEquals(1, recordCountAfterInsert - recordCountFirst, "실제로 insert 되지 않음");
        assertEquals(p1.getBudgetPartySeq(), list2.get(0).getBudgetPartySeq(), "PartySeq 값이 다름");
        assertEquals(p1.getBudgetMemberSeq(), list2.get(0).getBudgetMemberSeq(),"MemberSeq 값이 다름");
        assertEquals(p1.getBudgetGb(), list2.get(0).getBudgetGb(),"Gb 값이 다름");
        assertEquals(p1.getBudgetAmount(), list2.get(0).getBudgetAmount(),"Amount 값이 다름");
        assertEquals("20"+p1.getBudgetDt(), list2.get(0).getBudgetDt(),"Dt 값이 다름");
        assertEquals(p1.getBudgetPic(), list2.get(0).getBudgetPic(),"Pic 값이 다름");
    }

    @Test
    void patchBudget() {
        List<GetBudgetRes> list1 = mapper.getBudget(budgetPartySeq, month);
        GetBudgetRes r1 = list1.get(0);
        long budgetSeq = r1.getBudgetSeq();
        int recordCountFirst = list1.size();

        PatchBudgetReq p1 = new PatchBudgetReq();
        p1.setBudgetSeq(budgetSeq);
        p1.setBudgetMemberSeq(2L);
        p1.setBudgetGb(1);
        p1.setBudgetAmount(20000);
        p1.setBudgetPic("test2.jpg");

        int affectedRows = mapper.patchBudget(p1);
        assertEquals(1, affectedRows);

        List<GetBudgetRes> list2 = mapper.getBudget(budgetPartySeq, month);
        GetBudgetRes r2 = list2.get(0);
        int recordCountAfterInsert = list2.size();

        assertEquals(0, recordCountAfterInsert - recordCountFirst, "레코드 수가 다름");
        assertNotEquals(r1, r2, "update 전과 후의 결과 값이 같음");
        assertNotEquals(r1.getBudgetMemberSeq(), r2.getBudgetMemberSeq(),"MemberSeq 값이 같음");
        assertNotEquals(r1.getBudgetGb(), r2.getBudgetGb(),"Gb 값이 같음");
        assertNotEquals(r1.getBudgetAmount(), r2.getBudgetAmount(),"Amount 값이 같음");
        assertNotEquals(r1.getBudgetPic(), r2.getBudgetPic(),"Pic 값이 같음");

        for(int i = 0; i<list1.size(); i++){
            if(r1.getBudgetSeq() == r2.getBudgetSeq()){
                assertNotEquals(list1.get(i), list2.get(i));
                continue;
            }
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    void patchBudgetNoPartySeq() {
        long budgetPartySeq = 10000;
        List<GetBudgetRes> list1 = mapper.getBudget(budgetPartySeq, month);

        PatchBudgetReq p1 = new PatchBudgetReq();
        p1.setBudgetMemberSeq(2L);
        p1.setBudgetGb(2);
        p1.setBudgetAmount(20000);
        p1.setBudgetPic("test2.jpg");

        int affectedRows = mapper.patchBudget(p1);
        assertEquals(0, affectedRows);

        List<GetBudgetRes> list2 = mapper.getBudget(budgetPartySeq, month);
        assertEquals(list1.size(), list2.size());

        for(int i = 0; i<list1.size(); i++){
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    void getBudget() {
        List<GetBudgetRes> list1 = mapper.getBudget(budgetPartySeq, month);
        GetBudgetRes result = list1.get(0);

        GetBudgetRes expectedRes = new GetBudgetRes();
        expectedRes.setBudgetSeq(375);
        expectedRes.setBudgetPartySeq(1);
        expectedRes.setBudgetMemberSeq(464);
        expectedRes.setBudgetGb(2);
        expectedRes.setBudgetAmount(74047);
        expectedRes.setBudgetDt("2024-07-06");
        expectedRes.setBudgetText("lorem");
        expectedRes.setBudgetPic("random.jpg");

        assertEquals(expectedRes, result, "결과 값 상이함");
    }

    @Test
    void getBudgetPic() {
        List<GetBudgetRes> list1 = mapper.getBudget(budgetPartySeq, month);
        String result = list1.get(0).getBudgetPic();

        GetBudgetRes expectedRes = new GetBudgetRes();
        expectedRes.setBudgetPic("random.jpg");

        assertEquals(expectedRes.getBudgetPic(), result, "결과 값 상이함");
    }

    @Test
    void deleteBudget() {
        List<GetBudgetRes> list1 = mapper.getBudget(budgetPartySeq, month);
        GetBudgetRes result = list1.get(0);
        int recordCountAfterFirst = list1.size();

        long affectedRows = mapper.deleteBudget(result.getBudgetSeq());
        assertEquals(1, affectedRows);

        List<GetBudgetRes> list2 = mapper.getBudget(budgetPartySeq, month);
        int recordCountAfterDelete = list2.size();

        assertEquals(1, recordCountAfterFirst - recordCountAfterDelete, "Delete 후에 레코드 수 똑같음");
        for (GetBudgetRes getBudgetRes : list2) {
            assertNotEquals(result, getBudgetRes);
        }
    }

}