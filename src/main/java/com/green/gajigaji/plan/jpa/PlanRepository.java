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

    @Query(value = "SELECT pm.planSeq AS planSeq, pm.planStartDt AS planStartDt, pm.planStartTime AS planStartTime, " +
            "pm.planCompleted AS planCompleted, pm.planLocation AS planLocation, cd.cdGbNm AS cdGbNm, " +
            "pm.planTitle AS planTitle, pm.planContents AS planContents " +
            "FROM PlanMaster pm " +
            "JOIN commonCd cd " +
            "ON pm.planCompleted = cd.cdGb " +
            "WHERE pm.partyMaster.partySeq = :partySeq " +
            "AND cd.cdMain = 'PC'")
    List<GetPlanResInterface> findAllByParty(Long partySeq);
}
