package org.antonakospanos.iot.atlas.dao.repository;

import org.antonakospanos.iot.atlas.dao.model.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConditionRepository extends JpaRepository<Condition, Long> {

	Condition findByAction_Id(Long actionId);

	Condition findByAlert_Id(Long alertId);

	List<Condition> findByAlert_IdNotNull();
}
