package org.antonakospanos.iot.atlas.dao.repository;

import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Module, Long> {

	List<Action> findByModuleId(Long moduleId);

}
