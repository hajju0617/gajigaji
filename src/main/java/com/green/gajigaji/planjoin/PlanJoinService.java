package com.green.gajigaji.planjoin;

import com.green.gajigaji.common.CheckMapper;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.member.jpa.MemberRepository;
import com.green.gajigaji.member.jpa.PartyMember;
import com.green.gajigaji.plan.jpa.PlanMaster;
import com.green.gajigaji.plan.jpa.PlanRepository;
import com.green.gajigaji.planjoin.jpa.PlanJoinRepository;
import com.green.gajigaji.planjoin.jpa.PlanMember;
import com.green.gajigaji.planjoin.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.Part;

import static com.green.gajigaji.planjoin.exception.ConstMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanJoinService {
//    private final PlanJoinMapper mapper;
    private final CheckMapper checkMapper;
    private final PlanJoinRepository repository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    public ResultDto<Integer> postPlanJoin(TogglePlanJoinReq p) {
        if (checkMapper.checkPlanJoin(p) != null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, ERROR_MESSAGE_1);
        } else {
            PlanMaster planMaster = planRepository.getReferenceById(p.getPlmemberPlanSeq());
            PartyMember partyMember = memberRepository.getReferenceById(p.getMemberSeq());
            PlanMember planMember = new PlanMember();
            planMember.setPlanMaster(planMaster);
            planMember.setPartyMember(partyMember);
            repository.save(planMember);
            return ResultDto.resultDto(HttpStatus.OK, 1, POST_SUCCESS_MESSAGE);
        }
    }

    public ResultDto<Integer> deletePlanJoin(TogglePlanJoinReq p) {
        if (checkMapper.checkPlanJoin(p) == null) {
            return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, NULL_ERROR_MESSAGE);
        } else {
            if(checkMapper.checkPlanCompleted(checkMapper.checkPlanJoin(p)) == 2){
                return ResultDto.resultDto(HttpStatus.BAD_REQUEST, 2, COMPLETED_PLAN);
            } else {
                PlanMaster planMaster = planRepository.getReferenceById(p.getPlmemberPlanSeq());
                PartyMember partyMember = memberRepository.getReferenceById(p.getMemberSeq());
                PlanMember planMember = new PlanMember();
                planMember.setPlanMaster(planMaster);
                planMember.setPartyMember(partyMember);
                repository.delete(planMember);
                return ResultDto.resultDto(HttpStatus.OK, 1, DELETE_SUCCESS_MESSAGE);
            }
        }
    }
}
