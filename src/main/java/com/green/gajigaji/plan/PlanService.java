package com.green.gajigaji.plan;

import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.party.jpa.PartyMaster;
import com.green.gajigaji.party.jpa.PartyRepository;
import com.green.gajigaji.plan.jpa.PlanMaster;
import com.green.gajigaji.plan.jpa.PlanRepository;
import com.green.gajigaji.plan.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
            PartyMaster partyMaster = partyRepository.findPartyByPartySeq(p.getPlanPartySeq());
            PlanMaster planMaster = new PlanMaster();
            planMaster.setPlanSeq(p.getPlanSeq());
            planMaster.setPartyMaster(partyMaster);
            planMaster.setPlanStartDt(p.getPlanStartDt());
            planMaster.setPlanStartTime(p.getPlanStartTime());
            planMaster.setPlanTitle(p.getPlanTitle());
            planMaster.setPlanContents(p.getPlanContents());
            planMaster.setPlanCompleted("1");
            repository.save(planMaster);
            return ResultDto.resultDto(HttpStatus.OK, 1, POST_SUCCESS_MESSAGE);
        }
    }

    @Transactional
    public ResultDto<Integer> patchPlan(PatchPlanReq p) {
        if (checkMapper.checkPlanSeq(p.getPlanSeq()) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PLAN);
        } else {
            mapper.patchPlan(p);
            return ResultDto.resultDto(HttpStatus.OK, 1, PATCH_SUCCESS_MESSAGE);
        }
    }

    @Transactional
    public ResultDto<Integer> patchPlanCompleted(long planSeq) {
        if (checkMapper.checkPlanSeq(planSeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PLAN);
        } else {
            repository.updatePlanByPlanSeq(planSeq);
            return ResultDto.resultDto(HttpStatus.OK, 1, PATCH_SUCCESS_MESSAGE);
        }
    }

    public ResultDto<List<GetPlanRes>> getPlanAll(long planPartySeq) {
        if (checkMapper.checkPlanPartySeq(planPartySeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else {
            List<GetPlanResInterface> list = repository.findAllByParty(planPartySeq);
            List<GetPlanRes> results = new ArrayList<>();
            for(GetPlanResInterface getPlanResInterface : list) {
                GetPlanRes item = new GetPlanRes();
                results.add(item);
                item.setPlanSeq(getPlanResInterface.getPlanSeq());
                item.setPlanStartDt(getPlanResInterface.getPlanStartDt());
                item.setPlanStartTime(getPlanResInterface.getPlanStartTime());
                item.setPlanCompleted(getPlanResInterface.getPlanCompleted());
                item.setCdNm(getPlanResInterface.getCdGbNm());
                item.setPlanTitle(getPlanResInterface.getPlanTitle());
                item.setPlanContents(getPlanResInterface.getPlanContents());
                item.setPlanLocation(getPlanResInterface.getPlanLocation());
            }
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, results);
        }
    }
    //getPlanParty
    public ResultDto<List<GetPlanRes>> getPlanParty(long planPartySeq, int limit) {
        if (checkMapper.checkPlanPartySeq(planPartySeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PARTY);
        } else {
            List<GetPlanResInterface> list = repository.findAllByPartyLimit(planPartySeq, limit);
            List<GetPlanRes> results = new ArrayList<>();
            for(GetPlanResInterface getPlanResInterface : list) {
                GetPlanRes item = new GetPlanRes();
                results.add(item);
                item.setPlanSeq(getPlanResInterface.getPlanSeq());
                item.setPlanPartySeq(getPlanResInterface.getPlanPartySeq());
                item.setPlanStartDt(getPlanResInterface.getPlanStartDt());
                item.setPlanStartTime(getPlanResInterface.getPlanStartTime());
                item.setPlanCompleted(getPlanResInterface.getPlanCompleted());
                item.setCdNm(getPlanResInterface.getCdGbNm());
                item.setPlanTitle(getPlanResInterface.getPlanTitle());
                item.setPlanContents(getPlanResInterface.getPlanContents());
                item.setPlanLocation(getPlanResInterface.getPlanLocation());
            }
            return ResultDto.resultDto(HttpStatus.OK, 1, GET_SUCCESS_MESSAGE, results);
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

    public ResultDto<Integer> deletePlan(Long planSeq) {
        if (checkMapper.checkPlanSeq(planSeq) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NOT_FOUND_PLAN);
        } else {
            repository.deleteById(planSeq);
            return ResultDto.resultDto(HttpStatus.OK, 1, DELETE_SUCCESS_MESSAGE);
        }
    }
}
