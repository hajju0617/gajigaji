package com.green.gajigaji.budget;

import com.green.gajigaji.budget.model.*;
import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.CustomFileUtils;
import com.green.gajigaji.common.model.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.green.gajigaji.budget.exception.ConstMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetService {
    
    private final BudgetMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final CheckMapper checkMapper;

    @Transactional
    public ResultDto<Integer> postBudget(MultipartFile budgetPic, PostBudgetReq p) {
        if (checkMapper.checkBudgetPartySeq(p.getBudgetPartySeq()) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else if (checkMapper.checkBudgetMemberSeq(p.getBudgetPartySeq(), p.getBudgetMemberSeq()) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_MEMBER);
        } else if (p.getBudgetPartySeq() == null || p.getBudgetMemberSeq() == null || p.getBudgetGb() == null ||
                p.getBudgetAmount() == null || p.getBudgetDt() == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NULL_ERROR_MESSAGE);
        } else if (p.getBudgetGb() != 1 && p.getBudgetGb() != 2) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, "budgetGb 값은 1(입금), 2(출금) 중에 입력하세요.");
        } else if (p.getBudgetAmount() <= 0) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, "budgetAmount은 1 이상의 값을 입력하세요.");
        } else {
            String saveFileName = customFileUtils.makeRandomFileName(budgetPic);
            p.setBudgetPic(saveFileName);
            mapper.postBudget(p);
            String path = String.format("budget/%d", p.getBudgetSeq());
            customFileUtils.makeFolders(path);
            String target = String.format("%s/%s", path, saveFileName);
            try {
                customFileUtils.transferTo(budgetPic, target);
            } catch (Exception e) {
                throw new RuntimeException(PIC_SAVE_ERROR);
            }
            return ResultDto.resultDto(HttpStatus.OK, 1, POST_SUCCESS_MESSAGE);
        }
    }

    @Transactional
    public ResultDto<Integer> patchBudget(MultipartFile budgetPic, PatchBudgetReq p) {
        if (checkMapper.checkBudgetSeq(p.getBudgetSeq()) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_BUDGET);
        } else if (p.getBudgetSeq() == null || p.getBudgetMemberSeq() == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NULL_ERROR_MESSAGE);
        } else if (p.getBudgetGb() != 1 && p.getBudgetGb() != 2) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, "budgetGb 값은 1(입금), 2(출금) 중에 입력하세요.");
        } else if (p.getBudgetAmount() <= 0) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, "budgetAmount은 1 이상의 값을 입력하세요.");
        } else {
            String path = String.format("budget/%d", p.getBudgetSeq());
            String saveFileName = customFileUtils.makeRandomFileName();
            String target = String.format("%s/%s", path, saveFileName);

            try {
                customFileUtils.deleteFolder(path);
                customFileUtils.makeFolders(path);
                customFileUtils.transferTo(budgetPic, target);
            } catch (Exception e) {
                throw new RuntimeException(PIC_SAVE_ERROR);
            }
            p.setBudgetPic(saveFileName);
            mapper.patchBudget(p);
            return ResultDto.resultDto(HttpStatus.OK, 1, PATCH_SUCCESS_MESSAGE);
        }
    }

    public ResultDto<List<GetBudgetRes>> getBudget(long budgetPartySeq, String month) {
        if (!Arrays.asList(monthList).contains(month)) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, "month 값은 01 ~ 12 사이의 값으로 입력해주세요.");
        } else if (checkMapper.checkBudgetPartySeq(budgetPartySeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else {
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, mapper.getBudget(budgetPartySeq, month));
        }
    }

    public ResultDto<GetBudgetPicRes> getBudgetPic(long budgetSeq) {
        if (checkMapper.checkBudgetSeq(budgetSeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_BUDGET);
        } else {
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, mapper.getBudgetPic(budgetSeq));
        }
    }

    public ResultDto<Long> deleteBudget(long budgetSeq) {
        if (checkMapper.checkBudgetSeq(budgetSeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_BUDGET);
        } else {
            mapper.deleteBudget(budgetSeq);
            return ResultDto.resultDto(HttpStatus.OK, 1, DELETE_SUCCESS_MESSAGE);
        }
    }

    public ResultDto<GetBudgetMemberRes> getBudgetMember(long budgetPartySeq, String month) {
        if (!Arrays.asList(monthList).contains(month)) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, "month 값은 01 ~ 12 사이의 값으로 입력해주세요.");
        } else if (checkMapper.checkBudgetPartySeq(budgetPartySeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else {
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, mapper.getBudgetMember(budgetPartySeq, month));
        }
    }

    public ResultDto<GetBudgetMonthlyRes> getBudgetMonthly(long budgetPartySeq, String month) {
        if (!Arrays.asList(monthList).contains(month)) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, "month 값은 01 ~ 12 사이의 값으로 입력해주세요.");
        } else if (checkMapper.checkBudgetPartySeq(budgetPartySeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else {
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, mapper.getBudgetMonthly(budgetPartySeq, month));
        }
    }

    public ResultDto<List<GetMemberListRes>> getMemberList(long memberPartySeq){
        if (checkMapper.checkBudgetPartySeq(memberPartySeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else {
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, mapper.getMemberList(memberPartySeq));
        }
    }
}