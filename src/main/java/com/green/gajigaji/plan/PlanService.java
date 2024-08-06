package com.green.gajigaji.plan;

import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.party.jpa.Party;
import com.green.gajigaji.party.jpa.PartyRepository;
import com.green.gajigaji.plan.jpa.Plan;
import com.green.gajigaji.plan.jpa.PlanRepository;
import com.green.gajigaji.plan.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.green.gajigaji.plan.exception.ConstMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanMapper mapper;
    private final CheckMapper checkMapper;
    private final PlanRepository repository;
    private final PartyRepository partyRepository;

    public ResultDto<Integer> postPlan(PostPlanReq p) {
        if (checkMapper.checkPlanPartySeq(p.getPlanPartySeq()) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else if (p.getPlanPartySeq() == null || p.getPlanStartDt() == null || p.getPlanStartTime() == null ||
                p.getPlanTitle() == null || p.getPlanContents() == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NULL_ERROR_MESSAGE);
        } else {
            Party party = partyRepository.findPartyByPartySeq(p.getPlanPartySeq());
            Plan plan = new Plan();
            plan.setPlanSeq(p.getPlanSeq());
            //plan.setParty(party);
            plan.setPlanStartDt(p.getPlanStartDt());
            plan.setPlanStartTime(p.getPlanStartTime());
            plan.setPlanTitle(p.getPlanTitle());
            plan.setPlanContents(p.getPlanContents());
            repository.save(plan);
            return ResultDto.resultDto(HttpStatus.OK, 1, POST_SUCCESS_MESSAGE);
        }
    }

    public ResultDto<Integer> patchPlan(PatchPlanReq p) {
        if (checkMapper.checkPlanSeq(p.getPlanSeq()) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PLAN);
        } else {
            mapper.patchPlan(p);
            return ResultDto.resultDto(HttpStatus.OK, 1, PATCH_SUCCESS_MESSAGE);
        }
    }

    public ResultDto<Integer> patchPlanCompleted(long planSeq) {
        if (checkMapper.checkPlanSeq(planSeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PLAN);
        } else {
            mapper.patchPlanCompleted(planSeq);
            return ResultDto.resultDto(HttpStatus.OK, 1, PATCH_SUCCESS_MESSAGE);
        }
    }

    public ResultDto<List<GetPlanRes>> getPlanAll(long planPartySeq) {
        if (checkMapper.checkPlanPartySeq(planPartySeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else {
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, mapper.getPlanAll(planPartySeq));
        }
    }

    public ResultDto<GetPlanRes> getPlan(long planSeq) {
        if (checkMapper.checkPlanSeq(planSeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PLAN);
        } else {
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, mapper.getPlan(planSeq));
        }
    }

    public ResultDto<List<GetPlanMemberRes>> getPlanMember(long planSeq) {
        if (checkMapper.checkPlanSeq(planSeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PLAN);
        } else {
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, mapper.getPlanMember(planSeq));
        }
    }

    public ResultDto<Integer> deletePlan(long planSeq) {
        if (checkMapper.checkPlanSeq(planSeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PLAN);
        } else {
            mapper.deletePlan(planSeq);
            return ResultDto.resultDto(HttpStatus.OK, 1, DELETE_SUCCESS_MESSAGE);
        }
    }
}
