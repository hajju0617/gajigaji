package com.green.gajigaji.plan.jpa;

import com.green.gajigaji.plan.model.GetPlanResInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends JpaRepository<PlanMaster, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE PlanMaster pm " +
            "set pm.planCompleted = '2' " +
            "WHERE pm.planSeq = :planSeq " +
            "AND pm.planCompleted = '1' ")
    void updatePlanByPlanSeq(Long planSeq);

    @Query(value = "SELECT pm.planSeq AS planSeq, pm.partyMaster.partySeq as planPartySeq, pm.planStartDt AS planStartDt, pm.planStartTime AS planStartTime, " +
            "pm.planCompleted AS planCompleted, pm.planLocation AS planLocation, cd.cdGbNm AS cdGbNm, " +
            "pm.planTitle AS planTitle, pm.planContents AS planContents " +
            "FROM PlanMaster pm " +
            "JOIN commonCd cd " +
            "ON pm.planCompleted = cd.cdGb " +
            "WHERE pm.partyMaster.partySeq = :partySeq " +
            "AND cd.cdMain = 'PC'")
    List<GetPlanResInterface> findAllByParty(Long partySeq);

    @Query(value = "SELECT pm.plan_seq AS planSeq, pm.plan_party_seq as planPartySeq, pm.plan_start_dt AS planStartDt, pm.plan_start_time AS planStartTime, " +
            "pm.plan_completed AS planCompleted, pm.plan_location AS planLocation, cd.cd_Gb_Nm AS cdGbNm, " +
            "pm.plan_title AS planTitle, pm.plan_contents AS planContents " +
            "FROM plan_master pm " +
            "JOIN common_cd cd " +
            "ON pm.plan_completed = cd.cd_Gb " +
            "WHERE pm.plan_party_seq = :partySeq " +
            "AND cd.cd_main = 'PC' " +
            "ORDER BY plan_seq DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<GetPlanResInterface> findAllByPartyLimit(Long partySeq, int limit);
}
