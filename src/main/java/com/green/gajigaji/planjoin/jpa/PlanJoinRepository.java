package com.green.gajigaji.planjoin.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlanJoinRepository extends JpaRepository<PlanMember, Long> {

}
